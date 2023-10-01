package com.reet.prep.academy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        viewModel.fetchCurrentAffairVideos(subjectID)
        Log.e("CourseVideosDocid", subjectID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCourseVideosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCourseVideos.observe(viewLifecycleOwner) {
            Log.e("Videos", it.toString())
            if (!viewModel.isVideosLoaded) {
                videoList.addAll(it)
                viewModel.isVideosLoaded = true
                videoModelAdapter.notifyDataSetChanged()
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
        Log.e("clicked", "true")
    }

}