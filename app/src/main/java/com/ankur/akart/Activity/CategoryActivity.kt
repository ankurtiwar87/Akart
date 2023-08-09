package com.ankur.akart.Activity

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.R
import com.ankur.akart.adapters.CategoryProductAdapter
import com.ankur.akart.adapters.ProductAdapter
import com.ankur.akart.databinding.ActivityCategoryBinding
import com.ankur.akart.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProduct(intent.getStringExtra("cat"))
    }

    private fun getProduct(CategoryName: String?) {

        var list1 = ArrayList<AddProductModel>()

        lifecycleScope.launch(Dispatchers.IO) {
            Firebase.firestore.collection("products").whereEqualTo("productCategory",CategoryName).get().await()
                .also {
                    for (doc in it.documents) {
                        val data = doc.toObject(AddProductModel::class.java)
                        list1.add(data!!)
                    }
                }

            withContext(Dispatchers.Main)
            {
                binding.RecyclerView.adapter=CategoryProductAdapter(applicationContext,list1)

            }
        }

    }
}