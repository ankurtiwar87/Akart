package com.ankur.akart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.akart.Activity.CategoryActivity
import com.ankur.akart.R
import com.ankur.akart.databinding.LayoutCategoryItemBinding
import com.ankur.akart.model.CategoryModel
import com.bumptech.glide.Glide

class CategoryAdapter(private val context: Context,private var list:ArrayList<CategoryModel>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    inner class CategoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val binding =LayoutCategoryItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var currentItem =list[position]
        holder.binding.textView2.text=currentItem.category
        Glide.with(context).load(currentItem.img).into(holder.binding.imageView)
        holder.itemView.setOnClickListener {
            Intent(context,CategoryActivity::class.java).also {
                 it.putExtra("cat",currentItem.category)
                 context.startActivity(it)
            }
        }
    }
}