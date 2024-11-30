package com.reet.prep.academy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.adapter.QuizzesItemAdapter
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.constants.Constants.Companion.QUIZ_ID
import com.reet.prep.academy.constants.Constants.Companion.QUIZ_TYPE_ID
import com.reet.prep.academy.constants.Constants.Companion.SUBJECT_DOCUMENT_ID
import com.reet.prep.academy.databinding.FragmentQuizzesListBinding
import com.reet.prep.academy.model.QuizModel
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class QuizzesList : Fragment(), QuizzesItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentQuizzesListBinding
    private lateinit var viewModel: TestSeriesViewModel
    private var testSeriesQuizzesList = mutableListOf<QuizModel>()
    private lateinit var quizzesItemAdapter: QuizzesItemAdapter
    private lateinit var subjectID: String
    private lateinit var collectionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[TestSeriesViewModel::class.java]
        subjectID = requireArguments().getString(SUBJECT_DOCUMENT_ID).toString()
        collectionId = requireArguments().getString(Constants.QUIZ_TYPE_ID).toString()
        if (!viewModel.isTestSeriesSubjectsLoaded) {
            viewModel.fetchTestSeriesQuizLiveData(subjectID, collectionId)
        }
        quizzesItemAdapter = QuizzesItemAdapter(requireContext(), testSeriesQuizzesList)
        initLiveDataObservers()
    }

    private fun initLiveDataObservers() {
        viewModel.getTestSeriesQuizList.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE
                    response.data?.let { testSeriesQuizzesList.addAll(it) }
                    quizzesItemAdapter.notifyDataSetChanged()
                    viewModel.isTestSeriesSubjectsLoaded = true
                }

                is NetworkResult.Loading -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizzesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvQuizzes.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = quizzesItemAdapter
        }
        quizzesItemAdapter.setOnItemClickListener(this)

    }

    override fun onClick(position: Int) {
        var bundle = bundleOf()
        bundle.putString(SUBJECT_DOCUMENT_ID, subjectID)
        bundle.putString(QUIZ_ID, testSeriesQuizzesList[position].id)
        bundle.putString(QUIZ_TYPE_ID, collectionId)
        (activity as MainActivity).safeNavigate(
            findNavController(),
            R.id.action_quizzesList_to_quizQuestions,
            bundle
        )
    }
}