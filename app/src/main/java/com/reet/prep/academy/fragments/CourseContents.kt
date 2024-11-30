package com.reet.prep.academy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.reet.prep.academy.adapter.CoursesContentPagerAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentCourseContentsBinding

class CourseContents : Fragment() {
    private lateinit var binding: FragmentCourseContentsBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: CoursesContentPagerAdapter
    private lateinit var subjectID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subjectID = requireArguments().getString(Constants.SUBJECT_DOCUMENT_ID).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseContentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = binding.vpCourseContents
        adapter = CoursesContentPagerAdapter(childFragmentManager, lifecycle,subjectID)
        viewPager2.adapter = adapter
        binding.tlCourseContents.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tlCourseContents.selectTab(binding.tlCourseContents.getTabAt(position))
            }
        })
    }
}