package com.ankur.akart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ankur.akart.R
import com.ankur.akart.adapters.CategoryAdapter
import com.ankur.akart.adapters.ProductAdapter
import com.ankur.akart.databinding.FragmentHomeBinding
import com.ankur.akart.model.AddProductModel
import com.ankur.akart.model.CategoryModel
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if (preference.getBoolean("isCart",false))
        {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        binding= FragmentHomeBinding.inflate(layoutInflater)
        getCategoryFromFireStore()
        getProductFromFireStore()
        getSliderImage()

        return binding.root

    }

    private fun getSliderImage() {

            Firebase.firestore.collection("slider").document("Items").get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.slider)

            }
    }

        private fun getProductFromFireStore() {
            var list1 = ArrayList<AddProductModel>()

            lifecycleScope.launch(Dispatchers.IO) {
                Firebase.firestore.collection("products").get().await()
                    .also {
                        for (doc in it.documents) {
                            val data = doc.toObject(AddProductModel::class.java)
                            list1.add(data!!)
                        }
                    }

                withContext(Dispatchers.Main)
                {
                    binding.ProductRecyclerView.adapter = ProductAdapter(requireContext(), list1)

                }
            }
        }

        private fun getCategoryFromFireStore() {

            var list = ArrayList<CategoryModel>()

            lifecycleScope.launch(Dispatchers.IO) {
                Firebase.firestore.collection("Category").get().await()
                    .also {
                        for (doc in it.documents) {
                            val data = doc.toObject(CategoryModel::class.java)
                            list.add(data!!)
                        }
                    }

                withContext(Dispatchers.Main) {

                    binding.CategoryRecyclerView.adapter = CategoryAdapter(requireContext(), list)
                }

            }

        }
    }

