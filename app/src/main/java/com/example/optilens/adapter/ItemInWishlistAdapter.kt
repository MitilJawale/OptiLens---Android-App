package com.example.optilens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.optilens.R
import com.example.optilens.dataclass.Product

class ItemInWishlistAdapter(private val context: Context, private val productList: List<Product>) :
    RecyclerView.Adapter<ItemInWishlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val textViewProductName: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewProductBrand: TextView = itemView.findViewById(R.id.textViewBrand)
        val textViewProductPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_in_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        // Bind the product details to the views in item_in_wishlist.xml
        holder.textViewProductName.text = product.productName
        holder.textViewProductBrand.text = product.brand
        holder.textViewProductPrice.text = "Price: $${product.price}"

        Glide.with(context).load(product.getImageUrls().first()).into(holder.imageViewProduct)


    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
