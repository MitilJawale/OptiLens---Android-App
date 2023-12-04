package com.example.optilens.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.optilens.R
import com.example.optilens.dataclass.CartItem
import com.example.optilens.dataclass.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemInCartAdapter(
    private val context: Context,
    private val productList: List<Product>
) : RecyclerView.Adapter<ItemInCartAdapter.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val textViewProductName: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewProductBrand: TextView = itemView.findViewById(R.id.textViewBrand)
        val textViewProductPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_in_cart, parent, false)
        return ViewHolder(view)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        // Bind the product details to the views in item_in_wishlist.xml
        holder.textViewProductName.text = product.productName
        holder.textViewProductBrand.text = product.brand
        holder.textViewProductPrice.text = "Price: $${product.price}"
        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.iv_deleteIcon)
        Glide.with(context).load(product.getImageUrls().first()).into(holder.imageViewProduct)

        btnDelete.setOnClickListener{
            val cartItem = CartItem(product?.productId.toString(), product?.productName.toString(), product?.price ?: 0.0)
            removeFromCart(cartItem)
            notifyDataSetChanged()

        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
    private fun getUserRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        return database.reference.child("Users").child(userId)
    }
    private fun removeFromCart(cartItem: CartItem) {
        val userRef = getUserRef()

        userRef.child("cart").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val currentCart = mutableListOf<CartItem>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CartItem::class.java)
                        if (item != null) {
                            currentCart.add(item)
                        }
                    }
                }

                // Check if the item is in the cart
                val existingItem = currentCart.find { it.productId == cartItem.productId }
                if (existingItem != null) {
                    // Item is in the cart, remove it
                    currentCart.remove(existingItem)
                    Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()
                    Log.d("ProductAdapter", "Item removed from cart: $existingItem")
                } else {
                    // Item is not in the cart
                    Toast.makeText(context, "Item not found in cart", Toast.LENGTH_SHORT).show()
                    Log.d("ProductAdapter", "Item not found in cart: $cartItem")
                }

                // Update the cart in the Realtime Database
                userRef.child("cart").setValue(currentCart)
                    .addOnSuccessListener {
                        // Cart updated successfully
                        Log.d("ProductAdapter", "Cart updated successfully")
                    }
                    .addOnFailureListener { e ->
                        // Handle the failure to update the cart
                        Log.e("ProductAdapter", "Error updating cart", e)
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("ProductAdapter", "Database error", error.toException())
            }
        })
    }

}
