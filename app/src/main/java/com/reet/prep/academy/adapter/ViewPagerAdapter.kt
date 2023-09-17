package com.reet.prep.academy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reet.prep.academy.fragments.Images
import com.reet.prep.academy.fragments.PDFs
import com.reet.prep.academy.fragments.Videos

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Images()
            }

            1 -> {
                Videos()
            }

            else -> {
                PDFs()
            }
        }
    }
}