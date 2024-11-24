package com.reet.prep.academy.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.reet.prep.academy.LoginActivity.Companion.mAuth
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.constants.Constants.Companion.VERIFICATION_ID
import com.reet.prep.academy.databinding.FragmentOtpBinding

class OtpFragment : Fragment() {
    private lateinit var binding: FragmentOtpBinding;
    private lateinit var storedVerificationId: String
    private lateinit var credential: PhoneAuthCredential
    private val TAG = OtpFragment::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storedVerificationId = arguments?.getString(VERIFICATION_ID).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayOfEditTexts = arrayOf(
            binding.digit1,
            binding.digit2,
            binding.digit3,
            binding.digit4,
            binding.digit5,
            binding.digit6
        )
        for (currEditText in arrayOfEditTexts) {
            currEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count == 0) {
                        previousEditText()?.requestFocus()
                    } else {
                        nextEditText()?.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

                private fun nextEditText(): EditText? {
                    val index = arrayOfEditTexts.indexOf(currEditText)
                    return if (index < arrayOfEditTexts.size - 1) {
                        arrayOfEditTexts[index + 1]
                    } else {
                        null
                    }
                }

                private fun previousEditText(): EditText? {
                    val index = arrayOfEditTexts.indexOf(currEditText)
                    return if (index > 0) {
                        arrayOfEditTexts[index - 1]
                    } else {
                        null
                    }
                }
            })
        }

        binding.btnStartNow.setOnClickListener {
            val enteredPin =
                binding.digit1.text.toString() + binding.digit2.text.toString() + binding.digit3.text.toString() +
                        binding.digit4.text.toString() + binding.digit5.text.toString() + binding.digit6.text.toString()

            if (this::storedVerificationId.isInitialized) {
                credential =
                    PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        enteredPin
                    )
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.tvError.visibility = View.GONE
                    Log.d(TAG, "signInWithCredential:success")
                    binding.pbOtpProgressBar.visibility = View.GONE
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.tvError.text =
                            (task.exception as FirebaseAuthInvalidCredentialsException).message
                        binding.tvError.visibility = View.VISIBLE
                        binding.pbOtpProgressBar.visibility = View.VISIBLE
                    }
                }
            }
    }

}