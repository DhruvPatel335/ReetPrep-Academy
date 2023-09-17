package com.reet.prep.academy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.repository.HomeRepository

class HomeViewModel:ViewModel() {
    private val homeRepository:HomeRepository = HomeRepository()
    private var homeOffersLiveData:MutableLiveData<List<String>> = MutableLiveData()
   var isOfferLoaded = false;
    init {
        homeOffersLiveData = homeRepository.getHomeOffers()
    }
    val getHomeOffers: LiveData<List<String>>
        get() = homeOffersLiveData

    fun fetchHomeOffers(){
        homeRepository.fetchHomeOffers()
    }
}