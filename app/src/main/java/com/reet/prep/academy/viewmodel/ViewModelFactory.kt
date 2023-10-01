package com.reet.prep.academy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> HomeViewModel()
        CurrentAffairViewModel::class.java->CurrentAffairViewModel()
        TestSeriesViewModel::class.java->TestSeriesViewModel()
        CoursesViewModel::class.java->CoursesViewModel()
        else -> throw IllegalArgumentException("No ViewModel registered for $modelClass")
    } as T

}