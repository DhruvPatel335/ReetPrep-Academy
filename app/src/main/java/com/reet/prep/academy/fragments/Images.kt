package com.reet.prep.academy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.VerticalViewPager
import com.reet.prep.academy.adapter.CurrentAffairImageAdapter
import com.reet.prep.academy.databinding.FragmentImagesBinding
import com.reet.prep.academy.viewmodel.CurrentAffairViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class Images : Fragment() {
    private lateinit var binding: FragmentImagesBinding
    private var sliderItems: MutableList<String> = mutableListOf()
    private lateinit var viewPager: VerticalViewPager
    private lateinit var currentAffairImageAdapter: CurrentAffairImageAdapter
    private lateinit var viewModel: CurrentAffairViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[CurrentAffairViewModel::class.java]
        if (!viewModel.isImagesLoaded) {
            viewModel.fetchCurrentAffairImages()
        }
        currentAffairImageAdapter = CurrentAffairImageAdapter(requireContext(), sliderItems)
        initLivedataObservers()
    }

    private fun initLivedataObservers() {
        viewModel.getCurrentAffairImages.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE
                    response.data?.let { sliderItems.addAll(it) }
                    viewModel.isImagesLoaded = true
                    currentAffairImageAdapter.notifyDataSetChanged()
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
        binding = FragmentImagesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.idVerticalViewPager
        viewPager.adapter = currentAffairImageAdapter
    }
}