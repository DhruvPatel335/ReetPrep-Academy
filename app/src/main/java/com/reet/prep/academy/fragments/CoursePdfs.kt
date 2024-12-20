package com.reet.prep.academy.fragments

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
import com.google.common.graph.Network
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.CurrentAffairPdfAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentCoursePdfsBinding
import com.reet.prep.academy.databinding.FragmentPDFsBinding
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.viewmodel.CoursesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class CoursePdfs : Fragment(), CurrentAffairPdfAdapter.OnItemClickListener {
    private lateinit var subjectID: String
    private lateinit var binding: FragmentCoursePdfsBinding
    private lateinit var viewModel: CoursesViewModel
    private lateinit var pdfAdapter: CurrentAffairPdfAdapter
    private var pdfListLiveData: MutableList<CAProduct> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subjectID = requireArguments().getString(Constants.SUBJECT_DOCUMENT_ID).toString()
        viewModel = ViewModelProvider(this, ViewModelFactory())[CoursesViewModel::class.java]
        if (!viewModel.isPdfsLoaded) {
            viewModel.fetchCoursePdfs(subjectID)
        }
        pdfAdapter = CurrentAffairPdfAdapter(requireContext(), pdfListLiveData)
        initLiveDataObserver()
    }

    private fun initLiveDataObserver() {
        viewModel.getCurrentAffairPdfs.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE
                    response.data?.let { pdfListLiveData.addAll(it) }
                    pdfAdapter.notifyDataSetChanged()
                    viewModel.isPdfsLoaded = true
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
        // Inflate the layout for this fragment
        binding = FragmentCoursePdfsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvPdfs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPdfs.adapter = pdfAdapter
        pdfAdapter.setOnItemClickListener(this)
    }

    override fun onClick(position: Int) {
        Log.e("clicked", "true")
        (activity as MainActivity).safeNavigate(
            findNavController(), R.id.action_courseContents_to_pdfViewer,
            bundleOf("pdfUrl" to pdfListLiveData[position].link)
        )
    }
}