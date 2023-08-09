package com.ankur.akart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.MainActivity
import com.ankur.akart.R
import com.ankur.akart.RoomDB.AppDatabase
import com.ankur.akart.RoomDB.ProductDao
import com.ankur.akart.RoomDB.ProductModel
import com.ankur.akart.databinding.ActivityProductDetailBinding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductDetails(intent.getStringExtra("id"))


    }

    private fun getProductDetails(productId: String?) {

            Firebase.firestore.collection("products").document(productId!!).get().addOnSuccessListener {

                val list = it.get("productImage") as ArrayList<String>


                val name =it.getString("productName")
                val productSp=it.getString("productSP")

                binding.textView8.text=name
                binding.textView9.text=productSp
                binding.textView10.text=it.getString("productDescription")

                val slideList = ArrayList<SlideModel>()

                for (data in list)
                {
                    slideList.add(SlideModel(data,ScaleTypes.CENTER_CROP))
                }
                binding.imageSlider.setImageList(slideList)


                    cartAction(productId,name, it.getString("productCoverImg"),productSp)



            }.addOnFailureListener {
                Toast.makeText(this@ProductDetailActivity, "Failed to load", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(productId: String, name: String?, CoverImg: String?, productSp: String?) {

            val productDao = AppDatabase.getDatabase(applicationContext).productDao()

            if (productDao.isExist(productId) != null) {

                    binding.button4.text = "Go To Cart"

            } else {
                binding.button4.text = "Add To Cart"

            }

            binding.button4.setOnClickListener {

                if (productDao.isExist(productId) != null) {
                    openCart()
                } else {
                    addToCart(productDao, productId, name, productSp, CoverImg)
                }


        }
    }


    private fun addToCart(productDao: ProductDao, productId: String, name: String?, productSp: String?, coverImg: String?) {

        val data = ProductModel(productId,name, coverImg,productSp)

        lifecycleScope.launch (Dispatchers.IO){

            productDao.insertProduct(data)
            withContext(Dispatchers.Main)
            {

                binding.button4.text = "Go To Cart"

            }

        }



        }


    private fun openCart() {

        val preference=this.getSharedPreferences("info", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }


}
