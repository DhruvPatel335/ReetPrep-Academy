package com.reet.prep.academy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.model.CAProduct
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.model.VideoModel
import com.reet.prep.academy.repository.AuthenticationRepository
import com.reet.prep.academy.repository.CoursesRepository

class CoursesViewModel : ViewModel() {
    private val coursesRepository: CoursesRepository = CoursesRepository()
    private var coursesSubjectsLiveData: MutableLiveData<NetworkResult<List<TestSubject>>> = MutableLiveData()
    private val authenticationRepository: AuthenticationRepository = AuthenticationRepository()
    private var courseVideosLivedata: MutableLiveData<NetworkResult<List<VideoModel>>> = MutableLiveData()
    private var coursePdfsLiveData: MutableLiveData<NetworkResult<List<CAProduct>>> = MutableLiveData()
    var isPdfsLoaded: Boolean = false
    var isCourseLoaded = false
    var isVideosLoaded = false
    init {
        coursesSubjectsLiveData = coursesRepository.getCoursesSubjectLiveData()
        courseVideosLivedata = coursesRepository.getCourseVideoLiveData()
        coursePdfsLiveData = coursesRepository.getCoursePdfs()

    }

    val getCoursesSubjectLiveData: MutableLiveData<NetworkResult<List<TestSubject>>>
        get() = coursesSubjectsLiveData

    val getCourseVideos: MutableLiveData<NetworkResult<List<VideoModel>>>
        get() = courseVideosLivedata

    val getCurrentAffairPdfs: MutableLiveData<NetworkResult<List<CAProduct>>>
        get() = coursePdfsLiveData

    fun fetchCoursesSubjects(){
        coursesRepository.fetchCourseSubjects()
    }

    fun isCoursePurchased(courseId:String, userId:String,callback:(Boolean) -> Unit){
        authenticationRepository.isCoursePurchases(courseId,userId, callback)
    }

    fun fetchCurrentAffairVideos(courseId:String){
        coursesRepository.fetchCourseVideos(courseId)
    }

    fun fetchCoursePdfs(courseId: String) {
        coursesRepository.fetchCoursePdfs(courseId)
    }
}