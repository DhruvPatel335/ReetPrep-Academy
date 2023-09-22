package com.reet.prep.academy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.repository.TestSeriesRepository

class TestSeriesViewModel : ViewModel() {
    private val testSeriesRepository: TestSeriesRepository = TestSeriesRepository()
    private var testSeriesSubjectLiveData: MutableLiveData<List<TestSubject>> = MutableLiveData()
    private var testSeriesQuizListLiveData: MutableLiveData<List<String>> = MutableLiveData()
    var isTestSeriesLoaded = false
    var isTestSeriesSubjectsLoaded = false

    init {
        testSeriesSubjectLiveData = testSeriesRepository.getTestSeriesSubjects()
        testSeriesQuizListLiveData = testSeriesRepository.getTestQuizNameSubjects()
    }

    val getTestSeriesSubjectLiveData: MutableLiveData<List<TestSubject>>
        get() = testSeriesSubjectLiveData
    val getTestSeriesQuizList: MutableLiveData<List<String>>
        get() = testSeriesQuizListLiveData

    fun fetchTestSeriesSubjectLiveData() {
        testSeriesRepository.fetchTestSeriesSubjects()
    }

    fun fetchTestSeriesQuizLiveData(documentId: String) {
        testSeriesRepository.fetchQuizNameList(documentId)
    }
}