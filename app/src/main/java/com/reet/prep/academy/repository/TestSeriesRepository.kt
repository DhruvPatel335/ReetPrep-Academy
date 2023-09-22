package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.model.TestSubject

class TestSeriesRepository {
    private val dbAuthors = Firebase.firestore
    private var testSeriesSubjectLiveData: MutableLiveData<List<TestSubject>> = MutableLiveData()
    private var testSeriesQuizNameLiveData: MutableLiveData<List<String>> = MutableLiveData()

    fun getTestSeriesSubjects(): MutableLiveData<List<TestSubject>> {
        return testSeriesSubjectLiveData
    }

    fun getTestQuizNameSubjects(): MutableLiveData<List<String>> {
        return testSeriesQuizNameLiveData
    }

    fun fetchTestSeriesSubjects() {
        var testSubject = mutableListOf<TestSubject>()
        dbAuthors.collection("test_series")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    testSubject.add(
                        TestSubject(
                            document.data["subjectName"].toString(),
                            document.data["thumbnail"].toString(),
                            document.id
                        )
                    )
                }
                testSeriesSubjectLiveData.value = testSubject
                Log.e("List", testSubject.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchQuizNameList(documentId: String) {
        var quizzes = mutableListOf<String>()
        dbAuthors.collection("test_series").document(documentId).collection("Quizzes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e("DocumentId", document.id)
                    quizzes.add(document.data["Name"].toString())
                }
                testSeriesQuizNameLiveData.value = quizzes
                Log.e("ListQuizzes", quizzes.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

}