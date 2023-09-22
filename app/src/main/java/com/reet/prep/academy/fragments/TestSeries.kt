package com.reet.prep.academy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.TestSeriesSubjectAdapter
import com.reet.prep.academy.constants.Constants.Companion.SUBJECT_DOCUMENT_ID
import com.reet.prep.academy.databinding.FragmentTestSeriesBinding
import com.reet.prep.academy.model.TestSubject
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var bundle = bundleOf()
        bundle.putString(SUBJECT_DOCUMENT_ID, testSeriesSubjectList[position].documentId)
        findNavController().navigate(R.id.action_testSeries_to_quizzesList, bundle)
    }


}