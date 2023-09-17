package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class HomeRepository {
    private val dbAuthors = Firebase.firestore
    private val homeOffersLiveData: MutableLiveData<List<String>> =
        MutableLiveData()
    fun getHomeOffers(): MutableLiveData<List<String>>{
        return homeOffersLiveData
    }
    fun fetchHomeOffers(){
        var offers = mutableListOf<String>()
        dbAuthors.collection("homeOffers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                   offers.add(document.data["url"].toString())
                }
                homeOffersLiveData.value = offers
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }


    }
}