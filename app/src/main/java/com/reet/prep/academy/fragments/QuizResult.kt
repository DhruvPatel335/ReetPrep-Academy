package com.reet.prep.academy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reet.prep.academy.R
import com.reet.prep.academy.constants.Constants
import com.reet.prep.academy.databinding.FragmentQuizResultBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizResult : Fragment() {
    private lateinit var binding: FragmentQuizResultBinding

    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(Constants.ATTEMPTED_QUESTIONS)
            param2 = it.getString(Constants.CORRECT_QUESTIONS)
            param3 = it.getString(Constants.INCORRECT_QUESTIONS)
            param4 = it.getString(Constants.TOTAL_QUESTIONS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAttemptedQuestions.text = param1
        binding.tvCorrectQuestions.text = param2
        binding.tvInCorrectQuestions.text = param3
        binding.tvTotalQuestions.text = param4
    }
}