package com.ankur.akart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.akart.Activity.CategoryActivity
import com.ankur.akart.Activity.ProductDetailActivity
import com.ankur.akart.R
import com.ankur.akart.databinding.CategoryActivityBinding
import com.ankur.akart.model.AddProductModel
import com.ankur.akart.model.CategoryModel
import com.bumptech.glide.Glide

class CategoryProductAdapter(private val context: Context,private val list:ArrayList<AddProductModel>)
    :RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
        {

            val binding=CategoryActivityBinding.bind(itemView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_activity,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context).load(currentItem.productCoverImg).into(holder.binding.imageView3)
        holder.binding.textView5.text=currentItem.productSP
        holder.binding.textView6.text=currentItem.productName

        holder.itemView.setOnClickListener {
          val intent =  Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("id",currentItem.productId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)

        }
    }
}