package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.reet.prep.academy.model.CAProduct

class CurrentAffairRepository {
    private val dbAuthors = Firebase.firestore
    private val storage = Firebase.storage
    private var currentAffairPdfsLiveData:MutableLiveData<List<CAProduct>> = MutableLiveData()
    private var currentAffairImagesLiveData:MutableLiveData<List<String>> = MutableLiveData()
    fun getCurrentAffairImages(): MutableLiveData<List<String>>{
        return currentAffairImagesLiveData
    }
    fun getCurrentAffairPdfs(): MutableLiveData<List<CAProduct>>{
        return currentAffairPdfsLiveData
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

    fun fetchCurrentAffairPdfs(){
        var pdfs = mutableListOf<CAProduct>()
        dbAuthors.collection("current_affair_pdfs")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    pdfs.add(CAProduct(document.data["name"].toString(),document.data["url"].toString()))
                }
                currentAffairPdfsLiveData.value = pdfs
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}