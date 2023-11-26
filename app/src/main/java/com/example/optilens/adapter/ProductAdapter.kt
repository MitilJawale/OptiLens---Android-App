package com.example.optilens.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.optilens.R
import com.example.optilens.dataclass.Accessory
import com.example.optilens.dataclass.ContactLens
import com.example.optilens.dataclass.Eyeglass
import com.example.optilens.dataclass.Product
import com.example.optilens.dataclass.ProductCategory
import com.example.optilens.dataclass.Sunglass
import com.squareup.picasso.Picasso
import com.squareup.picasso.Callback


class ProductAdapter(private val context: Context,
                     private val productList: List<Product>?,
                     private val PROD: ProductCategory
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_ProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tv_ProductPrice)
        var productImage: ImageView = itemView.findViewById(R.id.iv_product)
        // Add other views as needed for product details



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)

        holder.productName.text = product?.productName
        holder.productPrice.text = product?.price.toString()
        holder.productImage.setImageResource(R.drawable.hard_wear_fv)


        // Find the eyeglass or sunglass with the matching productName
        val eyeglass = product?.let { findEyeglass(it.productName) }
        val sunglass = product?.let { findSunglass(it.productName) }
        val contactlens = product?.let { findContactlens(it.productName) }
        val accessories = product?.let { findAccessories(it.productName) }


        // Toggle WishList sign
        val wishlistImage = holder.itemView.findViewById<ImageView>(R.id.iv_wishlistImage)
        wishlistImage.setOnClickListener {
            // Check the color of the drawable and set the color filter accordingly
            val colorFilter = if (wishlistImage.colorFilter == null) {
                PorterDuffColorFilter(
                    ContextCompat.getColor(context, R.color.red),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }
            wishlistImage.colorFilter = colorFilter
        }


        // Load image using Picasso based on the product type
        when (product) {
            is Eyeglass -> loadImageWithPicasso(eyeglass!!, holder.productImage)
            is Sunglass -> loadImageWithPicasso(sunglass!!, holder.productImage)
            is ContactLens -> loadImageWithPicasso(contactlens!!, holder.productImage)
            is Accessory -> loadImageWithPicasso(accessories!!, holder.productImage)
        }


    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }


    // Function to find eyeglass with matching productName
    private fun findEyeglass(productName: String): Eyeglass? {
        // Filter the eyeglasses based on productName
        return productList?.filterIsInstance<Eyeglass>()
            ?.find { it.productName == productName }
    }

    private fun findSunglass(productName: String): Sunglass? {
        // Filter the eyeglasses based on productName
        return productList?.filterIsInstance<Sunglass>()
            ?.find { it.productName == productName }
    }

    private fun findContactlens(productName: String): ContactLens? {
        // Filter the eyeglasses based on productName
        return productList?.filterIsInstance<ContactLens>()
            ?.find { it.productName == productName }
    }

    private fun findAccessories(productName: String): Accessory? {
        // Filter the eyeglasses based on productName
        return productList?.filterIsInstance<Accessory>()
            ?.find { it.productName == productName }
    }


    private fun getProductImageURL(product: Product): String? {
        return when (product) {
            is Eyeglass -> product.images.frontView
            is Sunglass -> product.images.frontView
            is ContactLens -> product.images.frontView
            is Accessory -> product.images.frontView
            else -> null
        }
    }



    // Function to load image using Picasso
    private fun loadImageWithPicasso(product: Product, productImage: ImageView) {
        Picasso.get()
            .load(getProductImageURL(product))
            .placeholder(R.drawable.contact_lens) // Replace with your placeholder drawable
            .error(R.drawable.eye_care) // Replace with your error drawable
            .into(productImage, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                }

                override fun onError(e: Exception?) {
                    // Handle error here
                    Log.e("Picasso", "Error loading image", e)
                }
            })
    }

}

