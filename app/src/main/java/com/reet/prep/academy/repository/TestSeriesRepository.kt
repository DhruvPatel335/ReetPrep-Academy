package com.reet.prep.academy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.model.QuestionsModel
import com.reet.prep.academy.model.QuizModel
import com.reet.prep.academy.model.TestSubject

class TestSeriesRepository {
    private val dbAuthors = Firebase.firestore
    private var testSeriesSubjectLiveData: MutableLiveData<List<TestSubject>> = MutableLiveData()
    private var testSeriesQuizNameLiveData: MutableLiveData<List<QuizModel>> = MutableLiveData()
    private var testSeriesQuizQuestionsLiveData: MutableLiveData<List<QuestionsModel>> =
        MutableLiveData()

    fun getTestSeriesSubjects(): MutableLiveData<List<TestSubject>> {
        return testSeriesSubjectLiveData
    }

    fun getTestQuizNameSubjects(): MutableLiveData<List<QuizModel>> {
        return testSeriesQuizNameLiveData
    }

    fun getTestSeriesQuestions(): MutableLiveData<List<QuestionsModel>> {
        return testSeriesQuizQuestionsLiveData
    }

    fun fetchTestSeriesSubjects(collectionId: String) {
        var testSubject = mutableListOf<TestSubject>()
        dbAuthors.collection(collectionId)
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
                testSeriesSubjectLiveData.value = testSubject
                Log.e("List", testSubject.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchQuizNameList(documentId: String, collectionId: String) {
        var quizzes = mutableListOf<QuizModel>()
        dbAuthors.collection(collectionId).document(documentId).collection("Quizzes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e("DocumentId", document.id)
                    quizzes.add(QuizModel(document.id, document.data["Name"].toString()))
                }
                testSeriesQuizNameLiveData.value = quizzes
                Log.e("ListQuizzes", quizzes.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun fetchQuestions(subjectId: String, quizId: String, collectionId: String) {
        var quizzes = mutableListOf<QuestionsModel>()
        dbAuthors.collection(collectionId).document(subjectId).collection("Quizzes")
            .document(quizId).collection("Questions")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e("DocumentId", document.id)
                    quizzes.add(
                        QuestionsModel(
                            document.data["Question"].toString(),
                            document.data["a"].toString(),
                            document.data["b"].toString(),
                            document.data["c"].toString(),
                            document.data["d"].toString(),
                            document.data["correct"].toString()
                        )
                    )
                }
                testSeriesQuizQuestionsLiveData.value = quizzes
                Log.e("ListQuizzes", quizzes.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

}