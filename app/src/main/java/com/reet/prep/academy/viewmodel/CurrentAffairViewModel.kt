package com.reet.prep.academy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.repository.CurrentAffairRepository

class CurrentAffairViewModel:ViewModel() {
    private val currentAffairRepository:CurrentAffairRepository = CurrentAffairRepository()
    private var currentAffairImagesLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private var isImagesLoaded:Boolean = false
    init {
        currentAffairImagesLiveData = currentAffairRepository.getCurrentAffairImages()
    }
    val getCurrentAffairImages: LiveData<List<String>>
        get() = currentAffairImagesLiveData

    fun fetchCurrentAffairImages(){
        currentAffairRepository.fetchCurrentAffairImages()
    }
}