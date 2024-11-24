package com.reet.prep.academy.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.reet.prep.academy.LoginActivity
import com.reet.prep.academy.LoginActivity.Companion.mAuth
import com.reet.prep.academy.MainActivity
import com.reet.prep.academy.R
import com.reet.prep.academy.constants.Constants.Companion.VERIFICATION_ID
import com.reet.prep.academy.databinding.FragmentLoginBinding
import java.util.concurrent.TimeUnit

class Login : Fragment() {
    private lateinit var storedVerificationId: String
    private lateinit var resendToken:
            PhoneAuthProvider.ForceResendingToken

    private lateinit var binding: FragmentLoginBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.btnGetOtp.setOnClickListener {
            initiateOtp()
        }
//        binding.btnSignIn.setOnClickListener {
//
//            if (this::storedVerificationId.isInitialized) {
//                credential =
//                    PhoneAuthProvider.getCredential(
//                        storedVerificationId,
//                        binding.etOTP.text.toString()
//                    )
//                signInWithPhoneAuthCredential(credential)
//            }
//        }
    }

    private fun initiateOtp() {
        binding.tvError.visibility = View.GONE
        binding.pbOtpProgressBar.visibility = View.VISIBLE
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91" + binding.etPhoneNumber.text.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w(TAG, "onVerificationFailed", e)

                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            binding.tvError.text = "Enter correct mobile number"
                        }

                        is FirebaseTooManyRequestsException -> {
                            binding.tvError.text = e.message
                        }

                        is FirebaseAuthMissingActivityForRecaptchaException -> {
                            binding.tvError.text = e.message
                        }

                        else -> {
                            binding.tvError.text = e.message
                        }
                    }
                    binding.tvError.visibility = View.VISIBLE
                    binding.pbOtpProgressBar.visibility = View.GONE
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    binding.tvError.visibility = View.GONE
                    binding.pbOtpProgressBar.visibility = View.GONE
                    Log.d(TAG, "onCodeSent:$verificationId")
                    Toast.makeText(requireContext(), "Otp sent", Toast.LENGTH_SHORT).show()
                    storedVerificationId = verificationId
                    resendToken = token
                    val bundle = bundleOf(
                        VERIFICATION_ID to storedVerificationId,
                    );
                    findNavController().navigate(R.id.action_login_to_otpFragment, bundle)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

}