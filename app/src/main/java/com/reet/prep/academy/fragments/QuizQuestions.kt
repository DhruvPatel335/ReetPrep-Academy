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
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.NetworkResult
import com.reet.prep.academy.R
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.constants.Constants.Companion.ATTEMPTED_QUESTIONS
import com.reet.prep.academy.constants.Constants.Companion.CORRECT_QUESTIONS
import com.reet.prep.academy.constants.Constants.Companion.INCORRECT_QUESTIONS
import com.reet.prep.academy.constants.Constants.Companion.TOTAL_QUESTIONS
import com.reet.prep.academy.databinding.FragmentQuizQuestionsBinding
import com.reet.prep.academy.model.QuestionsModel
import com.reet.prep.academy.viewmodel.TestSeriesViewModel
import com.reet.prep.academy.viewmodel.ViewModelFactory

class QuizQuestions : Fragment() {
    private lateinit var binding: FragmentQuizQuestionsBinding
    private lateinit var viewModel: TestSeriesViewModel
    private var questionList: MutableList<QuestionsModel> = mutableListOf()
    private lateinit var subjectID: String
    private lateinit var quizId: String
    private var position = 0
    private lateinit var collectionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[TestSeriesViewModel::class.java]
        subjectID = requireArguments().getString(Constants.SUBJECT_DOCUMENT_ID).toString()
        quizId = requireArguments().getString(Constants.QUIZ_ID).toString()
        collectionId = requireArguments().getString(Constants.QUIZ_TYPE_ID).toString()
        viewModel.fetchTestSeriesQuestions(subjectID, quizId, collectionId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizQuestionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTestSeriesQuestions.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbProgressBar.visibility = View.GONE

                    response.data?.let { questionList.addAll(it) }
                    loadQuestions(questionList[position], position)
                    Log.e("Questions", questionList.toString())
                }

                is NetworkResult.Loading -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.pbProgressBar.visibility = View.VISIBLE
                }
            }

        }

        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnOptionA.setOnClickListener {
            if (binding.btnOptionA.text == questionList[position].correct) {
                binding.btnOptionA.setBackgroundResource(R.drawable.correct_ans_border)
                viewModel.questionCorrect++
                viewModel.questionAttempted++
            } else {
                binding.btnOptionA.setBackgroundResource(R.drawable.incorrect_and_border)
                viewModel.questionsIncorrect++
                highlightCorrectOption()
            }
        }
        binding.btnOptionB.setOnClickListener {
            if (binding.btnOptionB.text == questionList[position].correct) {
                binding.btnOptionB.setBackgroundResource(R.drawable.correct_ans_border)
                viewModel.questionCorrect++
                viewModel.questionAttempted++
            } else {
                binding.btnOptionB.setBackgroundResource(R.drawable.incorrect_and_border)
                viewModel.questionsIncorrect++
                highlightCorrectOption()
            }
        }
        binding.btnOptionC.setOnClickListener {
            if (binding.btnOptionC.text == questionList[position].correct) {
                binding.btnOptionC.setBackgroundResource(R.drawable.correct_ans_border)
                viewModel.questionCorrect++
                viewModel.questionAttempted++
            } else {
                binding.btnOptionC.setBackgroundResource(R.drawable.incorrect_and_border)
                viewModel.questionsIncorrect++
                highlightCorrectOption()
            }
        }
        binding.btnOptionD.setOnClickListener {
            if (binding.btnOptionD.text == questionList[position].correct) {
                binding.btnOptionD.setBackgroundResource(R.drawable.correct_ans_border)
                viewModel.questionCorrect++
                viewModel.questionAttempted++
            } else {
                binding.btnOptionD.setBackgroundResource(R.drawable.incorrect_and_border)
                viewModel.questionsIncorrect++
                highlightCorrectOption()
            }
        }
        binding.btnNext.setOnClickListener {
            if (position < questionList.size - 1) {
                reloadPage()
                Log.e("Position", position.toString())
                loadQuestions(questionList[++position], position)
                Log.e("Position", position.toString())
            } else {
                var bundle = bundleOf()
                bundle.putString(ATTEMPTED_QUESTIONS, viewModel.questionAttempted.toString())
                bundle.putString(CORRECT_QUESTIONS, viewModel.questionCorrect.toString())
                bundle.putString(INCORRECT_QUESTIONS, viewModel.questionsIncorrect.toString())
                bundle.putString(TOTAL_QUESTIONS, questionList.size.toString())
                (activity as MainActivity).safeNavigate(
                    findNavController(),
                    R.id.action_quizQuestions_to_quizResult,
                    bundle
                )
            }
        }
    }

    private fun reloadPage() {
        binding.btnOptionA.setBackgroundResource(R.drawable.border)
        binding.btnOptionB.setBackgroundResource(R.drawable.border)
        binding.btnOptionC.setBackgroundResource(R.drawable.border)
        binding.btnOptionD.setBackgroundResource(R.drawable.border)
        binding.btnOptionD.isEnabled = true
        binding.btnOptionC.isEnabled = true
        binding.btnOptionA.isEnabled = true
        binding.btnOptionB.isEnabled = true
    }

    private fun highlightCorrectOption() {
        if (binding.btnOptionD.text == questionList[position].correct) {
            binding.btnOptionD.setBackgroundResource(R.drawable.correct_ans_border)
        } else if (binding.btnOptionC.text == questionList[position].correct
        ) {
            binding.btnOptionC.setBackgroundResource(R.drawable.correct_ans_border)

        } else if (binding.btnOptionB.text == questionList[position].correct) {
            binding.btnOptionB.setBackgroundResource(R.drawable.correct_ans_border)
        } else {
            binding.btnOptionA.setBackgroundResource(R.drawable.correct_ans_border)
        }
        binding.btnOptionD.isEnabled = false
        binding.btnOptionC.isEnabled = false
        binding.btnOptionA.isEnabled = false
        binding.btnOptionB.isEnabled = false
    }

    private fun loadQuestions(questionsModel: QuestionsModel, position: Int) {
        binding.apply {
            tvQuestion.text = questionsModel.question
            btnOptionA.text = questionsModel.optionA
            btnOptionB.text = questionsModel.optionB
            btnOptionC.text = questionsModel.optionC
            btnOptionD.text = questionsModel.optionD
        }
        if (position == questionList.size - 1) {
            binding.btnNext.text = "Submit"
        }
    }
}