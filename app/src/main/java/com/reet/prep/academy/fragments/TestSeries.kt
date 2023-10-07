package com.reet.prep.academy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.TestSeriesSubjectAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.constants.Constants.Companion.QUIZ_TYPE_ID
import com.reet.prep.academy.constants.Constants.Companion.SUBJECT_DOCUMENT_ID
import com.reet.prep.academy.constants.Constants.Companion.TEST_SERIES
import com.reet.prep.academy.databinding.FragmentTestSeriesBinding
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory
import org.json.JSONObject
import kotlin.math.roundToInt

class TestSeries : Fragment(), TestSeriesSubjectAdapter.OnItemClickListener {
    private val dbAuthors = Firebase.firestore
    private lateinit var binding: FragmentTestSeriesBinding
    private lateinit var viewModel: TestSeriesViewModel
    private var testSeriesSubjectList = mutableListOf<TestSubject>()
    private lateinit var testSeriesSubjectAdapter: TestSeriesSubjectAdapter
    private lateinit var user: FirebaseUser
    private lateinit var phoneNumber: String
    private lateinit var collectionId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser!!
        phoneNumber = user.phoneNumber.toString()
        viewModel = ViewModelProvider(this, ViewModelFactory())[TestSeriesViewModel::class.java]
        collectionId = arguments?.getString(Constants.QUIZ_TYPE_ID)!!
        viewModel.fetchTestSeriesSubjectLiveData(collectionId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTestSeriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTestSeriesSubjectLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (!viewModel.isTestSeriesLoaded) {
                        binding.pbProgressBar.visibility = View.GONE
                        response.data?.let { testSeriesSubjectList.addAll(it) }
                        testSeriesSubjectAdapter.notifyDataSetChanged()
                        viewModel.isTestSeriesLoaded = true
                    }
                }

                is NetworkResult.Loading -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }
            }

        }
        testSeriesSubjectAdapter = TestSeriesSubjectAdapter(requireContext(), testSeriesSubjectList)
        binding.rvTestSeriesSubjects.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = testSeriesSubjectAdapter
        }
        testSeriesSubjectAdapter.setOnItemClickListener(this)

    }

    override fun onClick(position: Int) {
        val courseId = testSeriesSubjectList[position].documentId
        val userId = Firebase.auth.uid!!
        if (collectionId == TEST_SERIES) {
            checkForPurchase(courseId, userId, position)
        } else {
            Log.e("purchased", true.toString())
            var bundle = bundleOf()
            bundle.putString(SUBJECT_DOCUMENT_ID, testSeriesSubjectList[position].documentId)
            bundle.putString(QUIZ_TYPE_ID, collectionId)
            findNavController().navigate(R.id.action_testSeries_to_quizzesList, bundle)
        }
    }

    private fun checkForPurchase(courseId: String, userId: String, position: Int) {
        viewModel.isCoursePurchased(courseId, userId) { isCoursePurchased ->
            if (isCoursePurchased) {
                Log.e("purchased", true.toString())
                var bundle = bundleOf()
                bundle.putString(SUBJECT_DOCUMENT_ID, testSeriesSubjectList[position].documentId)
                bundle.putString(QUIZ_TYPE_ID, collectionId)
                findNavController().navigate(R.id.action_testSeries_to_quizzesList, bundle)
            } else {
                Log.e("purchased", false.toString())
                val paymentRequest = JSONObject()
                val paymentInitiator = JSONObject()
                val amount = testSeriesSubjectList[position].amount.roundToInt() * 100
                val description = "Purchase for " + testSeriesSubjectList[position].testSubject
                paymentInitiator.put("phoneNumber", phoneNumber)
                paymentInitiator.put("courseId", courseId)
                paymentInitiator.put("uid", userId)
                paymentRequest.put("name", "ReetPrepAcademy")
                paymentRequest.put("description", description)
                paymentRequest.put("theme.color", "")
                paymentRequest.put("currency", "INR")
                paymentRequest.put("amount", amount)
                paymentRequest.put("prefill.contact", phoneNumber)
                paymentRequest.put("prefill.email", "")
                (context as MainActivity).pay(paymentRequest, paymentInitiator)
            }
        }
    }
}