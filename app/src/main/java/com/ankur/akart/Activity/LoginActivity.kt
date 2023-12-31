package com.ankur.akart.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ankur.akart.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button5.setOnClickListener {
            Intent(this,RegisterActivity::class.java).also {
                startActivity(it)
            }
        }


        binding.button3.setOnClickListener {
            if (binding.PhoneNumber.text.toString().isEmpty())
            {
                Toast.makeText(this, "Please Enter the Phone Number", Toast.LENGTH_SHORT).show()
            }

            else
            {
                sendOtp(binding.PhoneNumber.text.toString())
            }
        }


    }

    private lateinit var dialogBox: AlertDialog

    private fun sendOtp(number: String) {

         dialogBox = AlertDialog.Builder(this)
            .setTitle("Loading........")
            .setMessage("Wait")
            .setCancelable(false)
            .create()

        dialogBox.show()

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


   val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            dialogBox.dismiss()
            Intent(this@LoginActivity,OtpActivity::class.java).also {
                it.putExtra("VerificationId",verificationId)
                it.putExtra("number",binding.PhoneNumber.text.toString())
                startActivity(it)
                finish()
            }

        }
    }
}