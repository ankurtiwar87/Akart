package com.ankur.akart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.akart.R
import com.ankur.akart.RoomDB.AppDatabase
import com.ankur.akart.RoomDB.ProductModel
import com.ankur.akart.databinding.FragmentCartItemBinding
import com.ankur.akart.model.AddProductModel
import com.bumptech.glide.Glide

class CartAdapter(private val context: Context,private val list:ArrayList<ProductModel>):RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding=FragmentCartItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_cart_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem =list[position]
        Glide.with(context).load(currentItem.ProductCoverImg).into(holder.binding.imageView3)
        holder.binding.textView6.text=currentItem.ProductName
        holder.binding.textView5.text=currentItem.ProductSP
        val dao = AppDatabase.getDatabase(context).productDao()
        holder.binding.delete.setOnClickListener {

            dao.deleteProduct(ProductModel(currentItem.ProductId,currentItem.ProductName,currentItem.ProductCoverImg,currentItem.ProductSP))
        }
    }
}