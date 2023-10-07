package com.reet.prep.academy.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.VideoModelAdapter
import com.reet.prep.academy.databinding.FragmentPDFsBinding
import com.reet.prep.academy.databinding.FragmentVideosBinding
import com.reet.prep.academy.model.VideoModel
import com.reet.prep.academy.viewmodel.CurrentAffairViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Videos.newInstance] factory method to
 * create an instance of this fragment.
 */
class Videos : Fragment(), VideoModelAdapter.OnItemClickListener {
    private lateinit var binding: FragmentVideosBinding
    private lateinit var viewModel: CurrentAffairViewModel
    private lateinit var videoModelAdapter: VideoModelAdapter
    private var videoList: MutableList<VideoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[CurrentAffairViewModel::class.java]
        viewModel.fetchCurrentAffairVideos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrentAffairVideos.observe(viewLifecycleOwner) {response->
            when(response){
                is NetworkResult.Success->{
                    Log.e("Videos", response.toString())
                    if (!viewModel.isVideosLoaded) {
                        response.data?.let { videoList.addAll(it) }
                        viewModel.isVideosLoaded = true
                        videoModelAdapter.notifyDataSetChanged()
                    }
                }
                is NetworkResult.Loading->{

                }
                is NetworkResult.Failure->{

                }
            }

        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        videoModelAdapter = VideoModelAdapter(requireContext(), videoList)
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