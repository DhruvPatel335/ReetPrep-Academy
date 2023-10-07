package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.model.VideoModel

class CurrentAffairRepository {
    private val dbAuthors = Firebase.firestore
    private val storage = Firebase.storage
    private var currentAffairPdfsLiveData: MutableLiveData<NetworkResult<List<CAProduct>>> = MutableLiveData()
    private var currentAffairImagesLiveData: MutableLiveData<NetworkResult<List<String>>> = MutableLiveData()
    private var currentAffairVideosLiveData: MutableLiveData<NetworkResult<List<VideoModel>>> = MutableLiveData()
    fun getCurrentAffairImages(): MutableLiveData<NetworkResult<List<String>>> {
        return currentAffairImagesLiveData
    }

    fun getCurrentAffairPdfs(): MutableLiveData<NetworkResult<List<CAProduct>>> {
        return currentAffairPdfsLiveData
    }

    fun getCurrentAffairVideos(): MutableLiveData<NetworkResult<List<VideoModel>>> {
        return currentAffairVideosLiveData
    }

    fun fetchCurrentAffairImages() {
        var images = mutableListOf<String>()
        currentAffairImagesLiveData.postValue(NetworkResult.Loading())
        dbAuthors.collection("current_affair_images")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    images.add(document.data["url"].toString())
                }
                currentAffairImagesLiveData.value = NetworkResult.Success(images)
            }
            .addOnFailureListener { exception ->
                currentAffairImagesLiveData.postValue(NetworkResult.Failure(exception.message))

                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCurrentAffairPdfs() {
        var pdfs = mutableListOf<CAProduct>()
        currentAffairPdfsLiveData.postValue(NetworkResult.Loading())

        dbAuthors.collection("current_affair_pdfs")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    pdfs.add(
                        CAProduct(
                            document.data["name"].toString(),
                            document.data["url"].toString()
                        )
                    )
                }
                currentAffairPdfsLiveData.value = NetworkResult.Success(pdfs)
            }
            .addOnFailureListener { exception ->
                currentAffairPdfsLiveData.postValue(NetworkResult.Failure(exception.message))

                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCurrentAffairVideos() {
        var videos = mutableListOf<VideoModel>()
        currentAffairVideosLiveData.postValue(NetworkResult.Loading())

        dbAuthors.collection("current_affair_videos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    videos.add(
                        VideoModel(
                            document.data["name"].toString(),
                            document.data["url"].toString(),
                            document.data["thumbnail"].toString()
                        )
                    )
                }
                currentAffairVideosLiveData.value = NetworkResult.Success(videos)
            }
            .addOnFailureListener { exception ->
                currentAffairVideosLiveData.postValue(NetworkResult.Failure(exception.message))
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}