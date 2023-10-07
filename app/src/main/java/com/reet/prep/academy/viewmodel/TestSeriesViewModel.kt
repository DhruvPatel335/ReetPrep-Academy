package com.reet.prep.academy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.model.QuestionsModel
import com.reet.prep.academy.model.QuizModel
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.repository.AuthenticationRepository
import com.reet.prep.academy.repository.TestSeriesRepository

class TestSeriesViewModel : ViewModel() {
    private val testSeriesRepository: TestSeriesRepository = TestSeriesRepository()
    private val authenticationRepository: AuthenticationRepository = AuthenticationRepository()
    private var testSeriesSubjectLiveData: MutableLiveData<NetworkResult<List<TestSubject>>> =
        MutableLiveData()
    private var testSeriesQuizListLiveData: MutableLiveData<NetworkResult<List<QuizModel>>> =
        MutableLiveData()
    private var testSeriesQuestionsListLiveData: MutableLiveData<NetworkResult<List<QuestionsModel>>> =
        MutableLiveData()
    var isTestSeriesLoaded = false
    var isTestSeriesSubjectsLoaded = false
    var isQuestionsLoaded = false

    init {
        testSeriesSubjectLiveData = testSeriesRepository.getTestSeriesSubjects()
        testSeriesQuizListLiveData = testSeriesRepository.getTestQuizNameSubjects()
        testSeriesQuestionsListLiveData = testSeriesRepository.getTestSeriesQuestions()
    }

    val getTestSeriesSubjectLiveData: MutableLiveData<NetworkResult<List<TestSubject>>>
        get() = testSeriesSubjectLiveData
    val getTestSeriesQuizList: MutableLiveData<NetworkResult<List<QuizModel>>>
        get() = testSeriesQuizListLiveData

    val getTestSeriesQuestions: MutableLiveData<NetworkResult<List<QuestionsModel>>>
        get() = testSeriesQuestionsListLiveData

    fun fetchTestSeriesSubjectLiveData(collectionId: String) {
        testSeriesRepository.fetchTestSeriesSubjects(collectionId)
    }

    fun fetchTestSeriesQuizLiveData(documentId: String, collectionId: String) {
        testSeriesRepository.fetchQuizNameList(documentId, collectionId)
    }

    fun fetchTestSeriesQuestions(subjectId: String, quizId: String, collectionId: String) {
        testSeriesRepository.fetchQuestions(subjectId, quizId, collectionId)
    }

    fun isCoursePurchased(courseId: String, userId: String, callback: (Boolean) -> Unit) {
        authenticationRepository.isCoursePurchases(courseId, userId, callback)
    }
}