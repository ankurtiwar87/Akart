package com.ankur.akart.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.MainActivity
import com.ankur.akart.R
import com.ankur.akart.RoomDB.AppDatabase
import com.ankur.akart.RoomDB.ProductModel
import com.ankur.akart.databinding.ActivityCheckOutBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject


private lateinit var binding: ActivityCheckOutBinding
class checkOutActivity : AppCompatActivity() , PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

      val price=  intent.getStringExtra("Total Cost")

        val activity: Activity = this


        val checkout=Checkout()
        checkout.setKeyID("rzp_test_XR48Ftp0X4DNow")

        try {
            val options = JSONObject()
            options.put("name","Akart")
            options.put("description","E-Commerce App")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#A3BAC3");
            options.put("currency","INR");
            options.put("amount",(price!!.toInt()*100))//pass amount in currency subunits

            val retryObj =  JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","ankurtiwar2002@gmail.com")
            prefill.put("contact","7983140772")

            options.put("prefill",prefill)
            checkout.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }



    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
        uploadData()
    }

    private fun uploadData() {
        val id =intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {
        Firebase.firestore.collection("products").document(productId!!).get()
            .addOnSuccessListener {
                saveData(productId,it.getString("productName"),it.getString("productSP"))
            }

    }

    private fun saveData(productId: String?, productName: String?, productSP: String?) {

        val preferences= this.getSharedPreferences("user", MODE_PRIVATE)

        val map = hashMapOf<String,Any>()
        map["productId"]=productId!!
        map["name"]=productName!!
        map["price"]=productSP!!
        map["status"]="ordered"
        map["userId"]=preferences.getString("number","")!!


       val firebase =Firebase.firestore.collection("allOrders")
        val key = firebase.document().id
        map["orderId"]=key

        val dao = AppDatabase.getDatabase(this).productDao()
        lifecycleScope.launch (Dispatchers.IO){

            firebase.add(map).await()
            dao.deleteProduct(ProductModel(productId))


            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Order Placed", Toast.LENGTH_SHORT).show()
                Intent(applicationContext,MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }

        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
    }
}