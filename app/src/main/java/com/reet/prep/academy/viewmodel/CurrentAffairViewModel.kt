package com.reet.prep.academy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.repository.CurrentAffairRepository

class CurrentAffairViewModel : ViewModel() {
    private val currentAffairRepository: CurrentAffairRepository = CurrentAffairRepository()
    private var currentAffairImagesLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private var currentAffairPdfsLiveData: MutableLiveData<List<CAProduct>> = MutableLiveData()

    var isImagesLoaded: Boolean = false

    init {
        currentAffairImagesLiveData = currentAffairRepository.getCurrentAffairImages()
        currentAffairPdfsLiveData = currentAffairRepository.getCurrentAffairPdfs()
    }

    val getCurrentAffairImages: LiveData<List<String>>
        get() = currentAffairImagesLiveData

    val getCurrentAffairPdfs: LiveData<List<CAProduct>>
        get() = currentAffairPdfsLiveData

    fun fetchCurrentAffairImages() {
        currentAffairRepository.fetchCurrentAffairImages()
    }

    fun fetchCurrentAffairPds() {
        currentAffairRepository.fetchCurrentAffairPdfs()
    }
}