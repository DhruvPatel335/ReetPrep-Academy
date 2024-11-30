package com.reet.prep.academy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.VideoModelAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentCourseVideosBinding
import com.reet.prep.academy.databinding.FragmentVideosBinding
import com.reet.prep.academy.model.VideoModel
import com.reet.prep.academy.viewmodel.CoursesViewModel
import com.reet.prep.academy.viewmodel.CurrentAffairViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class CourseVideos : Fragment(), VideoModelAdapter.OnItemClickListener {
    private lateinit var subjectID: String
    private lateinit var viewModel: CoursesViewModel
    private lateinit var videoModelAdapter: VideoModelAdapter
    private lateinit var binding: FragmentCourseVideosBinding
    private var videoList: MutableList<VideoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subjectID = requireArguments().getString(Constants.SUBJECT_DOCUMENT_ID).toString()
        viewModel = ViewModelProvider(this, ViewModelFactory())[CoursesViewModel::class.java]
        if (!viewModel.isVideosLoaded) {
            viewModel.fetchCurrentAffairVideos(subjectID)
        }
        videoModelAdapter = VideoModelAdapter(requireContext(), videoList)
        initLivedataObserver()
    }

    private fun initLivedataObserver() {
        viewModel.getCourseVideos.observe(this) { response ->
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
        binding = FragmentCourseVideosBinding.inflate(layoutInflater, container, false)
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
        (activity as MainActivity).safeNavigate(
            findNavController(), R.id.action_courseContents_to_playVideo,
            bundleOf("key" to videoList[position].videoUrl)
        )
    }
}