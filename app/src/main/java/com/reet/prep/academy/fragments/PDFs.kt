package com.reet.prep.academy.fragments

import android.content.Intent
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.CurrentAffairPdfAdapter
import com.reet.prep.academy.databinding.FragmentPDFsBinding
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.viewmodel.CurrentAffairViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class PDFs : Fragment(), CurrentAffairPdfAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPDFsBinding
    private lateinit var viewModel: CurrentAffairViewModel
    private var pdfListLiveData: MutableList<CAProduct> = mutableListOf()
    private lateinit var pdfAdapter: CurrentAffairPdfAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[CurrentAffairViewModel::class.java]
        if (!viewModel.isPdfsLoaded) {
            viewModel.fetchCurrentAffairPds()
        }
        pdfAdapter = CurrentAffairPdfAdapter(requireContext(), pdfListLiveData)
        initLiveDataObservers()
    }

    private fun initLiveDataObservers() {
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
        binding = FragmentPDFsBinding.inflate(layoutInflater, container, false)
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
        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfListLiveData[position].link))
        startActivity(intent)
    }
}