package com.reet.prep.academy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
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
        viewModel.fetchCurrentAffairImages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentImagesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.idVerticalViewPager
        currentAffairImageAdapter = CurrentAffairImageAdapter(requireContext(), sliderItems)
        viewPager.adapter = currentAffairImageAdapter
        viewModel.getCurrentAffairImages.observe(viewLifecycleOwner) {response->
            when(response){
                is NetworkResult.Success->{
                    if (!viewModel.isImagesLoaded){
                        binding.pbProgressBar.visibility = View.GONE
                        response.data?.let { sliderItems.addAll(it) }
                        viewModel.isImagesLoaded = true
                        currentAffairImageAdapter.notifyDataSetChanged()
                    }
                }
                is NetworkResult.Loading->{
                    binding.pbProgressBar.visibility = View.VISIBLE

                }
                is NetworkResult.Failure->{
                    binding.pbProgressBar.visibility = View.VISIBLE

                }
            }
        }
    }


}