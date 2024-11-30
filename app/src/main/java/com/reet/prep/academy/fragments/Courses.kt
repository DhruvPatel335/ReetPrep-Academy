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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.TestSeriesSubjectAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentCoursesBinding
import com.reet.prep.academy.databinding.FragmentTestSeriesBinding
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.viewmodel.CoursesViewModel
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory
import org.json.JSONObject
import kotlin.math.roundToInt

class Courses : Fragment(), TestSeriesSubjectAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCoursesBinding
    private lateinit var viewModel: CoursesViewModel
    private var coursesSubjectLiveData = mutableListOf<TestSubject>()
    private lateinit var testSeriesSubjectAdapter: TestSeriesSubjectAdapter
    private lateinit var user: FirebaseUser
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser!!
        phoneNumber = user.phoneNumber.toString()
        viewModel = ViewModelProvider(this@Courses, ViewModelFactory())[CoursesViewModel::class.java]
        if (!viewModel.isCourseLoaded){
            viewModel.fetchCoursesSubjects()
        }
        testSeriesSubjectAdapter =
            TestSeriesSubjectAdapter(requireContext(), coursesSubjectLiveData)
        initObserver()
    }

    private fun initObserver() {
        coursesSubjectLiveData.clear()
        viewModel.getCoursesSubjectLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE
                    it.data?.let { it1 -> coursesSubjectLiveData.addAll(it1) }
                    testSeriesSubjectAdapter.notifyDataSetChanged()
                    viewModel.isCourseLoaded = true
                    Log.e("NetworkResult", "Success")
                }

                is NetworkResult.Loading -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                    Log.e("NetworkResult", "loading")
                }

                is NetworkResult.Failure -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                    Log.e("NetworkResult", "Failure")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {

        binding.rvCourseSubjects.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = testSeriesSubjectAdapter
        }
        testSeriesSubjectAdapter.setOnItemClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onClick(position: Int) {
        val courseId = coursesSubjectLiveData[position].documentId
        val userId = Firebase.auth.uid!!
        viewModel.isCoursePurchased(courseId, userId) { isCoursePurchased ->
            if (isCoursePurchased) {
                Log.e("purchased", true.toString())
                var bundle = bundleOf()
                bundle.putString(
                    Constants.SUBJECT_DOCUMENT_ID,
                    coursesSubjectLiveData[position].documentId
                )
                (activity as MainActivity).safeNavigate(
                    findNavController(),
                    R.id.action_courses_to_courseContents,
                    bundle
                )
            } else {
                Log.e("purchased", false.toString())
                val paymentRequest = JSONObject()
                val paymentInitiator = JSONObject()
                val amount = coursesSubjectLiveData[position].amount.roundToInt() * 100
                val description = "Purchase for " + coursesSubjectLiveData[position].testSubject
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
