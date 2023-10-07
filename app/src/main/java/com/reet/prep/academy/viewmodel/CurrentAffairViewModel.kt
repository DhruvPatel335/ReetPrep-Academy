package com.reet.prep.academy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.model.VideoModel
import com.reet.prep.academy.repository.CurrentAffairRepository

class CurrentAffairViewModel : ViewModel() {
    private val currentAffairRepository: CurrentAffairRepository = CurrentAffairRepository()
    private var currentAffairImagesLiveData: MutableLiveData<NetworkResult<List<String>>> = MutableLiveData()
    private var currentAffairPdfsLiveData: MutableLiveData<NetworkResult<List<CAProduct>>> = MutableLiveData()
    private var currentAffairVideosLiveData: MutableLiveData<NetworkResult<List<VideoModel>>> = MutableLiveData()
    var isImagesLoaded: Boolean = false
    var isPdfsLoaded: Boolean = false
    var isVideosLoaded: Boolean = false

    init {
        currentAffairImagesLiveData = currentAffairRepository.getCurrentAffairImages()
        currentAffairPdfsLiveData = currentAffairRepository.getCurrentAffairPdfs()
        currentAffairVideosLiveData = currentAffairRepository.getCurrentAffairVideos()
    }

    val getCurrentAffairImages: MutableLiveData<NetworkResult<List<String>>>
        get() = currentAffairImagesLiveData

    val getCurrentAffairPdfs: MutableLiveData<NetworkResult<List<CAProduct>>>
        get() = currentAffairPdfsLiveData

    val getCurrentAffairVideos:MutableLiveData<NetworkResult<List<VideoModel>>>
        get() = currentAffairVideosLiveData

    fun fetchCurrentAffairImages() {
        currentAffairRepository.fetchCurrentAffairImages()
    }

    fun fetchCurrentAffairPds() {
        currentAffairRepository.fetchCurrentAffairPdfs()
    }

    fun fetchCurrentAffairVideos(){
        currentAffairRepository.fetchCurrentAffairVideos()
    }
}