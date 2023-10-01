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
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.TestSeriesSubjectAdapter
import com.reet.prep.academy.constants.Constants.Companion.SUBJECT_DOCUMENT_ID
import com.reet.prep.academy.databinding.FragmentTestSeriesBinding
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory
import org.json.JSONObject
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TestSeries : Fragment(), TestSeriesSubjectAdapter.OnItemClickListener {
    private val dbAuthors = Firebase.firestore
    private lateinit var binding: FragmentTestSeriesBinding
    private lateinit var viewModel: TestSeriesViewModel
    private var testSeriesSubjectList = mutableListOf<TestSubject>()
    private lateinit var testSeriesSubjectAdapter: TestSeriesSubjectAdapter
    private lateinit var user: FirebaseUser
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser!!
        phoneNumber = user.phoneNumber.toString()
        viewModel = ViewModelProvider(this, ViewModelFactory())[TestSeriesViewModel::class.java]
        viewModel.fetchTestSeriesSubjectLiveData()
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
        viewModel.getTestSeriesSubjectLiveData.observe(viewLifecycleOwner) {
            if (!viewModel.isTestSeriesLoaded) {
                testSeriesSubjectList.addAll(it)
                testSeriesSubjectAdapter.notifyDataSetChanged()
                viewModel.isTestSeriesLoaded = true
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
        viewModel.isCoursePurchased(courseId, userId){
            isCoursePurchased ->
            if (isCoursePurchased){
                Log.e("purchased", true.toString())
                var bundle = bundleOf()
                bundle.putString(SUBJECT_DOCUMENT_ID, testSeriesSubjectList[position].documentId)
                findNavController().navigate(R.id.action_testSeries_to_quizzesList, bundle)
            }
            else{
                Log.e("purchased", false.toString())
                val paymentRequest = JSONObject()
                val paymentInitiator = JSONObject()
                val amount = testSeriesSubjectList[position].amount.roundToInt() * 100
                val description = "Purchase for "+testSeriesSubjectList[position].testSubject
                paymentInitiator.put("phoneNumber", phoneNumber)
                paymentInitiator.put("courseId", courseId)
                paymentInitiator.put("uid", userId)
                paymentRequest.put("name", "ReetPrepAcademy")
                paymentRequest.put("description",description )
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