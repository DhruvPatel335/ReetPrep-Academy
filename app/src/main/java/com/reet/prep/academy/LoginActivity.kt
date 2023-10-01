package com.reet.prep.academy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.reet.prep.academy.databinding.ActivityLoginBinding
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var storedVerificationId: String
    private lateinit var resendToken:
            PhoneAuthProvider.ForceResendingToken
    private lateinit var credential: PhoneAuthCredential
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        mAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.btnGetOtp.setOnClickListener {
            initiateOtp()
        }
        binding.btnSignIn.setOnClickListener {

            if (this::storedVerificationId.isInitialized) {
                credential =
                    PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        binding.etOTP.text.toString()
                    )
                signInWithPhoneAuthCredential(credential)
            }
        }

    }

    private fun initiateOtp() {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91" + binding.etPhoneNumber.text.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@LoginActivity)
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

                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    binding.tvError.visibility = View.GONE
                    Log.d(TAG, "onCodeSent:$verificationId")
                    Toast.makeText(this@LoginActivity, "Otp sent", Toast.LENGTH_SHORT).show()
                    storedVerificationId = verificationId
                    resendToken = token
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    binding.tvError.visibility = View.GONE
                    Log.d(TAG, "signInWithCredential:success")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(this@LoginActivity, "Logged In", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.tvError.text =
                            (task.exception as FirebaseAuthInvalidCredentialsException).message
                        binding.tvError.visibility = View.VISIBLE
                    }
                }
            }
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}