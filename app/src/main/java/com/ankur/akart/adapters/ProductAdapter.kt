package com.ankur.akart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.akart.Activity.ProductDetailActivity
import com.ankur.akart.R
import com.ankur.akart.databinding.LayoutProductItemBinding
import com.ankur.akart.model.AddProductModel
import com.bumptech.glide.Glide

class ProductAdapter(private val context: Context,private val list: ArrayList<AddProductModel>)
    :RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


        inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
        {
            val binding1=LayoutProductItemBinding.bind(itemView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_product_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context).load(currentItem.productCoverImg).into(holder.binding1.imageView2)
        holder.binding1.textView.text=currentItem.productName
        holder.binding1.textView3.text=currentItem.productCategory
        holder.binding1.textView4.text=currentItem.productMRP
        holder.binding1.button.text=currentItem.productSP
        holder.binding1.button.setOnClickListener {
            Intent(context, ProductDetailActivity::class.java).also {
                it.putExtra("id",currentItem.productId)
                context.startActivity(it)
            }
        }

        holder.binding1.button2.setOnClickListener {
            Intent(context, ProductDetailActivity::class.java).also {
                it.putExtra("id",currentItem.productId)
                context.startActivity(it)
            }
        }

        holder.itemView.setOnClickListener {
            Intent(context, ProductDetailActivity::class.java).also {
                it.putExtra("id",currentItem.productId)
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }


}