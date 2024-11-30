package com.reet.prep.academy.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.adapter.VideoModelAdapter
import com.reet.prep.academy.databinding.FragmentVideosBinding
import com.reet.prep.academy.model.VideoModel
import com.reet.prep.academy.viewmodel.CurrentAffairViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class Videos : Fragment(), VideoModelAdapter.OnItemClickListener {
    private lateinit var binding: FragmentVideosBinding
    private lateinit var viewModel: CurrentAffairViewModel
    private lateinit var videoModelAdapter: VideoModelAdapter
    private var videoList: MutableList<VideoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[CurrentAffairViewModel::class.java]
        if (!viewModel.isVideosLoaded) {
            viewModel.fetchCurrentAffairVideos()
        }
        videoModelAdapter = VideoModelAdapter(requireContext(), videoList)
        initLivedataObservers()
    }

    private fun initLivedataObservers() {
        viewModel.getCurrentAffairVideos.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE
                    response.data?.let { videoList.addAll(it) }
                    viewModel.isVideosLoaded = true
                    videoModelAdapter.notifyDataSetChanged()
                }

                is NetworkResult.Loading -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvVideos.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvVideos.adapter = videoModelAdapter
        videoModelAdapter.setOnItemClickListener(this)
    }

    override fun onClick(position: Int) {
        val videoIntent = Intent(Intent.ACTION_VIEW)
        videoIntent.data = Uri.parse(videoList[position].videoUrl)
        startActivity(videoIntent)
    }
}