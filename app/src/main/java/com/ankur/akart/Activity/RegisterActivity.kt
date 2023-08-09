package com.ankur.akart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.R
import com.ankur.akart.databinding.ActivityRegisterBinding
import com.ankur.akart.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Login.setOnClickListener{
            openLogin()

        }

        binding.signIn.setOnClickListener {

            ValidateUser()


        }
    }

    private fun ValidateUser() {
        if (binding.UserName.text.toString().isEmpty()){

            binding.UserName.requestFocus()
            binding.UserName.error="Empty"
        }
        else if (binding.PhoneNumber.text.toString().isEmpty())
        {
            binding.PhoneNumber.requestFocus()
            binding.PhoneNumber.error="Empty"

        }
        else if (binding.PhoneNumber.text.toString().length>10 || binding.PhoneNumber.text.toString().length<10)
        {
            binding.PhoneNumber.requestFocus()
            binding.PhoneNumber.error="Enter 10 Digit Number"

        }
        else
        {
            storeData()
        }
    }

    private fun storeData() {
        val dialogBox = AlertDialog.Builder(this)
            .setTitle("Loading.........")
            .setMessage("Wait")
            .setCancelable(false)
            .create()

        dialogBox.show()

        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        val editor =preferences.edit()
        editor.putString("number",binding.PhoneNumber.text.toString())
        editor.putString("name",binding.UserName.text.toString())
        editor.apply()

        val data = UserModel(Username =binding.UserName.text.toString(), UserNumber = binding.PhoneNumber.text.toString() )

        lifecycleScope.launch(Dispatchers.IO) {
            Firebase.firestore.collection("users").document(binding.PhoneNumber.text.toString()).set(data).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "User Registered", Toast.LENGTH_SHORT).show()
                dialogBox.dismiss()
                openLogin()
            }

        }


    }

    private fun openLogin() {
        Intent(this,LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}