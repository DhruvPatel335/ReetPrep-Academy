package com.reet.prep.academy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.CurrentAffairPdfAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentCoursePdfsBinding
import com.reet.prep.academy.databinding.FragmentPDFsBinding
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.viewmodel.CoursesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class CoursePdfs : Fragment(),CurrentAffairPdfAdapter.OnItemClickListener {
    private lateinit var subjectID: String
    private lateinit var binding: FragmentCoursePdfsBinding
    private lateinit var viewModel: CoursesViewModel
    private lateinit var pdfAdapter: CurrentAffairPdfAdapter
    private var pdfListLiveData: MutableList<CAProduct> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subjectID = requireArguments().getString(Constants.SUBJECT_DOCUMENT_ID).toString()
        viewModel = ViewModelProvider(this, ViewModelFactory())[CoursesViewModel::class.java]
        viewModel.fetchCoursePdfs(subjectID)
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
        viewModel.getCurrentAffairPdfs.observe(viewLifecycleOwner) {
            if (!viewModel.isPdfsLoaded) {
                pdfListLiveData.addAll(it)
                pdfAdapter.notifyDataSetChanged()
                viewModel.isPdfsLoaded = true
            }

        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        pdfAdapter = CurrentAffairPdfAdapter(requireContext(), pdfListLiveData)
        binding.rvPdfs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPdfs.adapter = pdfAdapter
        pdfAdapter.setOnItemClickListener(this)
    }

    override fun onClick(position: Int) {
        Log.e("clicked", "true")
    }


}