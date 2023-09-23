package com.reet.prep.academy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reet.prep.academy.model.QuestionsModel
import com.reet.prep.academy.model.QuizModel
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.repository.TestSeriesRepository

class TestSeriesViewModel : ViewModel() {
    private val testSeriesRepository: TestSeriesRepository = TestSeriesRepository()
    private var testSeriesSubjectLiveData: MutableLiveData<List<TestSubject>> = MutableLiveData()
    private var testSeriesQuizListLiveData: MutableLiveData<List<QuizModel>> = MutableLiveData()
    private var testSeriesQuestionsListLiveData: MutableLiveData<List<QuestionsModel>> =
        MutableLiveData()
    var isTestSeriesLoaded = false
    var isTestSeriesSubjectsLoaded = false
    var isQuestionsLoaded = false

    init {
        testSeriesSubjectLiveData = testSeriesRepository.getTestSeriesSubjects()
        testSeriesQuizListLiveData = testSeriesRepository.getTestQuizNameSubjects()
        testSeriesQuestionsListLiveData = testSeriesRepository.getTestSeriesQuestions()
    }

    val getTestSeriesSubjectLiveData: MutableLiveData<List<TestSubject>>
        get() = testSeriesSubjectLiveData
    val getTestSeriesQuizList: MutableLiveData<List<QuizModel>>
        get() = testSeriesQuizListLiveData

    val getTestSeriesQuestions: MutableLiveData<List<QuestionsModel>>
        get() = testSeriesQuestionsListLiveData

    fun fetchTestSeriesSubjectLiveData() {
        testSeriesRepository.fetchTestSeriesSubjects()
    }

    fun fetchTestSeriesQuizLiveData(documentId: String) {
        testSeriesRepository.fetchQuizNameList(documentId)
    }

    fun fetchTestSeriesQuestions(subjectId: String, quizId: String) {
        testSeriesRepository.fetchQuestions(subjectId, quizId)
    }
}