package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.model.VideoModel

class CoursesRepository {
    private val dbAuthors = Firebase.firestore
    private var coursesSubjectLiveData: MutableLiveData<NetworkResult<List<TestSubject>>> =
        MutableLiveData()
    private var courseVideoLiveData: MutableLiveData<NetworkResult<List<VideoModel>>> =
        MutableLiveData()
    private var coursePdfsLiveData: MutableLiveData<NetworkResult<List<CAProduct>>> =
        MutableLiveData()

    fun getCoursesSubjectLiveData(): MutableLiveData<NetworkResult<List<TestSubject>>> {
        return coursesSubjectLiveData
    }

    fun getCourseVideoLiveData(): MutableLiveData<NetworkResult<List<VideoModel>>> {
        return courseVideoLiveData
    }

    fun getCoursePdfs(): MutableLiveData<NetworkResult<List<CAProduct>>> {
        return coursePdfsLiveData
    }

    fun fetchCourseSubjects() {
        var testSubject = mutableListOf<TestSubject>()
        coursesSubjectLiveData.postValue(NetworkResult.Loading())
        dbAuthors.collection("courses")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    testSubject.add(
                        TestSubject(
                            document.data["subjectName"].toString(),
                            document.data["thumbnail"].toString(),
                            document.id,
                            document.data["amount"].toString().toFloat()
                        )
                    )
                }
                coursesSubjectLiveData.value = NetworkResult.Success(testSubject)
                Log.e("List", testSubject.toString())
            }
            .addOnFailureListener { exception ->
                coursesSubjectLiveData.value = NetworkResult.Failure(exception.toString())
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCourseVideos(courseId: String) {
        var videos = mutableListOf<VideoModel>()
        courseVideoLiveData.postValue(NetworkResult.Loading())
        dbAuthors.collection("courses").document(courseId).collection("video")
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
                courseVideoLiveData.value = NetworkResult.Success(videos)
            }
            .addOnFailureListener { exception ->
                courseVideoLiveData.value = NetworkResult.Failure(exception.message)
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCoursePdfs(courseId: String) {
        var pdfs = mutableListOf<CAProduct>()
        coursePdfsLiveData.postValue(NetworkResult.Loading())
        dbAuthors.collection("courses").document(courseId).collection("pdfs")
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
                coursePdfsLiveData.value = NetworkResult.Success(pdfs)
            }
            .addOnFailureListener { exception ->
                coursePdfsLiveData.postValue(NetworkResult.Failure(exception.message))

                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}