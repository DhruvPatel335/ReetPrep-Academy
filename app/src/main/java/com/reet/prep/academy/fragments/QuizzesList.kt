package com.reet.prep.academy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.reet.prep.academy.adapter.QuizzesItemAdapter
import com.reet.prep.academy.constants.Constants.Companion.SUBJECT_DOCUMENT_ID
import com.reet.prep.academy.databinding.FragmentQuizzesListBinding
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class QuizzesList : Fragment(), QuizzesItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentQuizzesListBinding
    private lateinit var viewModel: TestSeriesViewModel
    private var testSeriesQuizzesList = mutableListOf<String>()
    private lateinit var quizzesItemAdapter: QuizzesItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[TestSeriesViewModel::class.java]
        viewModel.fetchTestSeriesQuizLiveData(requireArguments().getString(SUBJECT_DOCUMENT_ID)!!)
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

        viewModel.getTestSeriesQuizList.observe(viewLifecycleOwner) {
            if (!viewModel.isTestSeriesSubjectsLoaded) {
                testSeriesQuizzesList.addAll(it)
                quizzesItemAdapter.notifyDataSetChanged()
                viewModel.isTestSeriesSubjectsLoaded = true
            }
        }
        quizzesItemAdapter = QuizzesItemAdapter(requireContext(), testSeriesQuizzesList)
        binding.rvQuizzes.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = quizzesItemAdapter
        }
        quizzesItemAdapter.setOnItemClickListener(this)

    }

    override fun onClick(position: Int) {

    }

}