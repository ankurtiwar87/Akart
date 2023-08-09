package com.ankur.akart.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ankur.akart.Activity.AddressActivity
import com.ankur.akart.Activity.CategoryActivity
import com.ankur.akart.R
import com.ankur.akart.RoomDB.AppDatabase
import com.ankur.akart.RoomDB.ProductModel
import com.ankur.akart.adapters.CartAdapter
import com.ankur.akart.databinding.FragmentCartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var list:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCartBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        list=ArrayList()
        val productDao = AppDatabase.getDatabase(requireContext()).productDao()

        productDao.getAllProduct().observe(this, Observer {
            lifecycleScope.launch(Dispatchers.Main) {

                list.clear()
                for (data in it){
                    list.add(data.ProductId)

                }
                binding.cartRecyclerView.adapter=CartAdapter(requireContext(),
                    it as ArrayList<ProductModel>)

            }
            totalPrice(it)


        })

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun totalPrice(data: List<ProductModel>?) {

        var total =0
        if (data != null) {
            for(item in data)
            {
                total+= item.ProductSP!!.toInt()
            }
        }

        if (data != null) {
            binding.textView7.text="Number of Item in Cart:${data.size} "
        }

        binding.textView11.text="Total Cost : ₹ ${total}"


        binding.checkout.setOnClickListener {
            Intent(requireContext(),AddressActivity::class.java).also {
                val b=Bundle()
                b.putStringArrayList("productIds",list)
                b.putString("Total Cost",total.toString())
                it.putExtras(b)
                startActivity(it)
            }

        }

    }


}