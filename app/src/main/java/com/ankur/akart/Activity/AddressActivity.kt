package com.ankur.akart.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences:SharedPreferences
    private lateinit var totalPrice:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences= this.getSharedPreferences("user", MODE_PRIVATE)
        totalPrice=intent.getStringExtra("Total Cost")!!
        loadInfo()
        binding.submitButton.setOnClickListener {
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.State.text.toString(),
                binding.City.text.toString(),
                binding.pinCode.text.toString()
            )
        }

    }

    private fun validateData(number: String, name: String,state:String, city: String, pinCode: String) {
        if (number.isEmpty()||name.isEmpty()||city.isEmpty()||pinCode.isEmpty()||state.isEmpty())
        {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
        }
        else
        {
            StoreData(state,city,pinCode)
        }

    }

    private fun loadInfo() {

            Firebase.firestore.collection("users")
                .document(preferences.getString("number","")!!).get()
                .addOnSuccessListener {

                    binding.userName.setText(it.getString("username"))
                    binding.userNumber.setText(it.getString("userNumber"))
                    binding.State.setText(it.getString("state"))
                    binding.City.setText(it.getString("city"))
                    binding.pinCode.setText(it.getString("pinCode"))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Data not fetch from User", Toast.LENGTH_SHORT).show()
                }
               


        

    }
    fun StoreData(state:String,city: String, pinCode: String) {
        val map = hashMapOf<String,Any>()
        map["state"]=state
        map["city"]=city
        map["pinCode"]=pinCode


        lifecycleScope.launch(Dispatchers.IO) {

            Firebase.firestore.collection("users")
                .document(preferences.getString("number","")!!).update(map).await()

            withContext(Dispatchers.Main)
            {
                val b=Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("Total Cost",totalPrice)
                Intent(applicationContext,checkOutActivity::class.java).also {
                    it.putExtras(b)
                    startActivity(it)
                }
            }
        }









    }
}


