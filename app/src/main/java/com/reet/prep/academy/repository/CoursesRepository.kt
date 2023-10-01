package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.model.VideoModel

class CoursesRepository {
    private val dbAuthors = Firebase.firestore
    private var coursesSubjectLiveData: MutableLiveData<List<TestSubject>> = MutableLiveData()
    private var courseVideoLiveData: MutableLiveData<List<VideoModel>> = MutableLiveData()
    private var coursePdfsLiveData: MutableLiveData<List<CAProduct>> = MutableLiveData()

    fun getCoursesSubjectLiveData(): MutableLiveData<List<TestSubject>> {
        return coursesSubjectLiveData
    }

    fun getCourseVideoLiveData(): MutableLiveData<List<VideoModel>> {
        return courseVideoLiveData
    }

    fun getCoursePdfs(): MutableLiveData<List<CAProduct>> {
        return coursePdfsLiveData
    }

    fun fetchCourseSubjects() {
        var testSubject = mutableListOf<TestSubject>()
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
                coursesSubjectLiveData.value = testSubject
                Log.e("List", testSubject.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCourseVideos(courseId:String) {
        var videos = mutableListOf<VideoModel>()
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
                courseVideoLiveData.value = videos
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchCoursePdfs(courseId:String) {
        var pdfs = mutableListOf<CAProduct>()
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
                coursePdfsLiveData.value = pdfs
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}