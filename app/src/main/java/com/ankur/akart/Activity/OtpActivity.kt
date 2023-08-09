package com.ankur.akart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ankur.akart.MainActivity
import com.ankur.akart.R
import com.ankur.akart.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.verify.setOnClickListener {
            if (binding.otp.text!!.toString().isEmpty())
            {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }

            else
            {
                verifyUser(binding.otp.text.toString())

            }
        }
    }

    private fun verifyUser(otp: String) {
        val verificationId = intent.getStringExtra("VerificationId")
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor =preferences.edit()
                    editor.putString("number",intent.getStringExtra("number"))
                    editor.apply()
//                    val user = task.result?.user
                      Intent(this,MainActivity::class.java).also {
                          startActivity(it)
                          finish()
                      }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}