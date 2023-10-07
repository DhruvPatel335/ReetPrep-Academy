package com.reet.prep.academy.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.SlideAdapter
import com.reet.prep.academy.constants.Constants.Companion.DAILY_QUIZZES
import com.reet.prep.academy.constants.Constants.Companion.QUIZ_TYPE_ID
import com.reet.prep.academy.constants.Constants.Companion.TEST_SERIES
import com.reet.prep.academy.databinding.FragmentHomeBinding
import com.reet.prep.academy.viewmodel.HomeViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory
import kotlin.math.abs

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var homeOffersList: MutableList<String> = mutableListOf()
    private lateinit var viewPager2: ViewPager2
    private lateinit var slideAdapter: SlideAdapter
    private var carousalRunnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }
    private var carousalHandler: Handler = Handler()
    private var resetCarousel = Runnable {
        viewPager2.currentItem = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[HomeViewModel::class.java]
        viewModel.fetchHomeOffers()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViewPager()
        setViewPagerAutoScroll()
    }

    private fun observeData() {
        viewModel.getHomeOffers.observe(viewLifecycleOwner) {
            if (!viewModel.isOfferLoaded) {
                homeOffersList.addAll(it)
                slideAdapter.notifyDataSetChanged()
            }
            Log.e("Homeoffers", homeOffersList.toString())
            viewModel.isOfferLoaded = true
        }
    }

    private fun initViewPager() {
        viewPager2 = binding.vpCarousel
        slideAdapter = SlideAdapter(requireContext(), homeOffersList, viewPager2)
        viewPager2.adapter = slideAdapter
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false
        viewPager2.offscreenPageLimit = 5
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setViewPagerAutoScroll() {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            var r: Float = abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                carousalHandler.removeCallbacks(carousalRunnable)
                carousalHandler.postDelayed(carousalRunnable, 3000)
                Log.e("pagePosition", position.toString())
                if (position == homeOffersList.size - 1) {
                    viewPager2.postDelayed(resetCarousel, 3000)
                }
            }

        }
        )
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.ilCurrentAffair.root.setOnClickListener {
            findNavController().navigate(R.id.action_homeNavigation_to_currentAffairNavigation)
        }
        binding.ilCourseTab.root.setOnClickListener {
            findNavController().navigate(R.id.action_homeNavigation_to_courses)
        }
        binding.ilDailyQuiz.root.setOnClickListener {
            findNavController().navigate(R.id.action_homeNavigation_to_testSeries, bundleOf(QUIZ_TYPE_ID to DAILY_QUIZZES))
        }
        binding.ilTestSeries.root.setOnClickListener {
            findNavController().navigate(R.id.action_homeNavigation_to_testSeries, bundleOf(QUIZ_TYPE_ID to TEST_SERIES))
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}