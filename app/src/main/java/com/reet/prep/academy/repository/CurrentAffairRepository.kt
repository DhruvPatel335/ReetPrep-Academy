package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CurrentAffairRepository {
    private val dbAuthors = Firebase.firestore
    private var currentAffairImagesLiveData:MutableLiveData<List<String>> = MutableLiveData()
    fun getCurrentAffairImages(): MutableLiveData<List<String>>{
        return currentAffairImagesLiveData
    }

    fun fetchCurrentAffairImages(){
        var images = mutableListOf<String>()
        dbAuthors.collection("current_affair_images")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    images.add(document.data["url"].toString())
                }
                currentAffairImagesLiveData.value = images
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

    }
}