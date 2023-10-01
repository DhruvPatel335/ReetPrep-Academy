package com.reet.prep.academy.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.fragments.CoursePdfs
import com.reet.prep.academy.fragments.CourseVideos
import com.reet.prep.academy.fragments.PDFs

class CoursesContentPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, val subjectID: String) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val courseVideos = CourseVideos()
                courseVideos.arguments = bundleOf(Constants.SUBJECT_DOCUMENT_ID to subjectID)
                return courseVideos
            }

            else -> {
                val pdfs = CoursePdfs()
                pdfs.arguments = bundleOf(Constants.SUBJECT_DOCUMENT_ID to subjectID)
                return pdfs
            }
        }
    }


}