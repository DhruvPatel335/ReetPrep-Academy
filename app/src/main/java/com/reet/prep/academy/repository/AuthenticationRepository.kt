package com.reet.prep.academy.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthenticationRepository {
    private val dbAuthors = Firebase.firestore

    fun isCoursePurchases(courseId: String, userId: String,callback:(Boolean)->Unit) {
        val purchasedCourse =
            dbAuthors.collection("purchase_data").whereEqualTo("courseId", courseId)
                .whereEqualTo("userId", userId)
        purchasedCourse.get().addOnSuccessListener { result ->
            for (document in result) {
                callback(true)
                return@addOnSuccessListener
            }
            callback(false)
        }
            .addOnFailureListener { exception ->
                Log.e("purchasedCourse", exception.toString())
                callback(false)
            }


    }

    fun addPurchase(courseId: String, phoneNumber: String, userId: String) {
        val data = hashMapOf(
            "courseId" to courseId,
            "userId" to userId,
            "phoneNumber" to phoneNumber
        )
        dbAuthors.collection("purchase_data")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
}