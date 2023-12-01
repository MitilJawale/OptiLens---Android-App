package com.example.optilens.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.optilens.R
import com.example.optilens.dataclass.Accessory
import com.example.optilens.dataclass.ContactLens
import com.example.optilens.dataclass.CartItem
import com.example.optilens.dataclass.Eyeglass
import com.example.optilens.dataclass.Product
import com.example.optilens.dataclass.ProductCategory
import com.example.optilens.dataclass.Sunglass
import com.example.optilens.fragments.ProductDescriptionFragment
import com.google.gson.Gson
import com.example.optilens.dataclass.WishlistItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import java.io.BufferedReader
import java.io.InputStreamReader
import com.squareup.picasso.Picasso


class ProductAdapter(private val context: Context,
                     private val productList: List<Product>?,
//                     private val PROD: ProductCategory
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_productName)
        val productPrice: TextView = itemView.findViewById(R.id.tv_ProductPrice)
        var productImage: ImageView = itemView.findViewById(R.id.iv_product)
        // Add other views as needed for product details


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList?.get(position)
                    Log.d("Adapter1: ", product?.productName.toString())
                    openProductImageViewFragment(product)
                }
            }
        }

        var addToCart :Button= itemView.findViewById(R.id.btn_addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)

        holder.productName.text = product?.productName
        holder.productPrice.text = "$".plus(product?.price.toString())
        holder.productImage.setImageResource(R.drawable.hard_wear_fv)

        val wishlistImage = holder.itemView.findViewById<ImageView>(R.id.iv_wishlistImage)

        // Find the eyeglass or sunglass with the matching productName
        val eyeglass = product?.let { findEyeglass(it.productName) }
        val sunglass = product?.let { findSunglass(it.productName) }
        val contactlens = product?.let { findContactlens(it.productName) }
        val accessories = product?.let { findAccessories(it.productName) }

        // Load image using Picasso based on the product type
        when (product) {
            is Eyeglass -> loadImageWithPicasso(eyeglass!!, holder.productImage)
            is Sunglass -> loadImageWithPicasso(sunglass!!, holder.productImage)
            is ContactLens -> loadImageWithPicasso(contactlens!!, holder.productImage)
            is Accessory -> loadImageWithPicasso(accessories!!, holder.productImage)
        }



        holder.addToCart.setOnClickListener {
            // Handle the click event for the addToCart button
            // For example, you can call the addToCart function here
            val cartItem = CartItem(product?.productId.toString(), product?.productName.toString(), product?.price ?: 0.0)
            addToCart(cartItem)
        }

        // Check if the productId is in the wishlist
        getWishlistProductIds { wishlistProductIds ->
            val isInWishlist = wishlistProductIds.contains(product?.productId)
            // Set the color filter for the wishlist image based on whether it's in the wishlist
            if (isInWishlist) {
                val colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, R.color.red),
                    PorterDuff.Mode.SRC_IN
                )
                wishlistImage.setColorFilter(colorFilter)
            } else {
                wishlistImage.clearColorFilter()
            }
        }

        // Toggle WishList sign
        wishlistImage.setOnClickListener {
            val productId = product?.productId ?: ""
            val productName = product?.productName ?: ""

            // Check the color of the drawable and set the color filter accordingly
            if (wishlistImage.colorFilter == null) {
                val colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, R.color.red),
                    PorterDuff.Mode.SRC_IN
                )

                // Log the product name when the wishlist button is clicked
                Log.d("Wishlist", "Product Name: $productName")
                val wl = WishlistItem(productId, productName)
                addToWishList(wl)
                wishlistImage.setColorFilter(colorFilter)
            } else {
                wishlistImage.clearColorFilter()
                Log.d("Wishlist", "Now you are here")
                val wl = WishlistItem(productId, productName)
                addToWishList(wl)
            }
        }
    }

    private fun getUserRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        return database.reference.child("Users").child(userId)
    }
    private fun getWishlistProductIds(callback: (List<String>) -> Unit) {
        val userRef = getUserRef()

        userRef.child("wishlist").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productIds = mutableListOf<String>()

                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(WishlistItem::class.java)
                        item?.productId?.let {
                            Log.d("Wishlist2",it)
                            productIds.add(it)
                        }
                    }
                }
                Log.d("ProductIds In Wishlist",productIds.toString())

                callback(productIds)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("ProductAdapter", "Database error", error.toException())
            }
        })
    }

    private fun addToWishList(wl: WishlistItem) {
            val userRef = getUserRef()
            userRef.child("wishlist").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentWishlist = mutableListOf<WishlistItem>()

                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val item = childSnapshot.getValue(WishlistItem::class.java)
                            if (item != null) {
                                currentWishlist.add(item)
                            }
                        }
                    }
                    // Check if the item is already in the wishlist
                    val existingItem = currentWishlist.find { it.productId == wl.productId }
                    if (existingItem == null) {
                        // Item is not in the wishlist, add it
                        currentWishlist.add(wl)
                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show()
                        Log.d("ProductAdapter", "Item added to wishlist: $wl")
                    } else {
                        // Item is already in the wishlist, remove it
                        currentWishlist.remove(existingItem)
                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                        Log.d("ProductAdapter", "Item removed from wishlist: $existingItem")
                    }

                    // Update the wishlist in the Realtime Database
                    userRef.child("wishlist").setValue(currentWishlist)
                        .addOnSuccessListener {
                            // Wishlist updated successfully
                            Log.d("ProductAdapter", "Wishlist updated successfully")
                        }
                        .addOnFailureListener { e ->
                            // Handle the failure to update the wishlist
                            Log.e("ProductAdapter", "Error updating wishlist", e)
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    Log.e("ProductAdapter", "Database error", error.toException())
                }
            })

    }
    private fun addToCart(ci: CartItem) {
        val userRef = getUserRef()

        // Fetch the existing cart to avoid overwriting it
        userRef.child("cart").addListenerForSingleValueEvent(object : ValueEventListener {
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
        // Load image using Picasso based on the product type


                // Add the new item to the cart
                currentCart.add(ci)

                // Update the cart in the Realtime Database
                userRef.child("cart").setValue(currentCart)
                    .addOnSuccessListener {
                        // Cart updated successfully
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                        Log.d("ProductAdapter", "Item added to cart: $ci")
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
            .placeholder(R.drawable.baseline_cached_24) // Replace with your placeholder drawable
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


    private fun openProductImageViewFragment(product: Product?) {
        val imageUrls = product?.getImageUrls() ?: emptyList()

        val fragment = ProductDescriptionFragment()
        val args = Bundle()

        args.putStringArrayList("imageUrls", ArrayList(imageUrls))
        args.putString("productName", product!!.productName)
        args.putString("brand", product.brand)
        args.putDouble("price", product.price)
        args.putDouble("ratings", product.ratings.averageRating)
        args.putString("framecolor", product.frameColor)
        args.putString("numberofreviews", product.ratings.numberOfRatings.toString())
        args.putString("productframestyle", product.productStyle)
        args.putString("height", product.dimensions.height.toString())
        args.putString("width", product.dimensions.width.toString())
        args.putString("bridgesize", product.dimensions.bridgeSize.toString())
        args.putString("templelength", product.dimensions.templeLength.toString())
        args.putString("category", product.category)
        args.putString("identifier", product.identifier)

        if(product.identifier == "contactlens") {
            args.putString("material", productList?.filterIsInstance<ContactLens>() ?.find { it.productName == product.productName }?.material)
            args.putString("watercontent", productList?.filterIsInstance<ContactLens>() ?.find { it.productName == product.productName }?.waterContent.toString())
            args.putString("spherePR", productList?.filterIsInstance<ContactLens>() ?.find { it.productName == product.productName }?.spherePowerRange)
            args.putString("cylinderPR", productList?.filterIsInstance<ContactLens>() ?.find { it.productName == product.productName }?.cylinderPowerRange)

        }

        if(product.identifier == "accessories") {
            if(product.productName == "Comprehensive Eyeglass Repair Kit") {
                args.putString("detail11", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.includes)
                args.putString("detail22", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.easyToUse)
                args.putString("detail33", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.versatile)

                args.putString("detail1", "Include")
                args.putString("detail2", "Easy to use")
                args.putString("detail3", "Versatility")
            }

            if(product.productName == "Anti-Fog and Smudge-Resistant Eyeglass Cleaning Spray") {
                args.putString("detail11", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.effectiveCleaning)
                args.putString("detail22", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.antiFogFormula)
                args.putString("detail33", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.streakFreeResults)

                args.putString("detail1", "Cleaning")
                args.putString("detail2", "Anti Fog Formula")
                args.putString("detail3", "Streak Free")
            }

            if(product.productName == "Adjustable Eyeglass Cord with Clasps") {
                args.putString("detail11", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.secureAttachment)
                args.putString("detail22", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.adjustableLength)
                args.putString("detail33", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.lightweightAndComfortable)

                args.putString("detail1", "Attachment")
                args.putString("detail2", "Adjustability")
                args.putString("detail3", "Weight")
            }

            if(product.productName == "Protective Eyeglass Case with Microfiber Cleaning Cloth") {
                args.putString("detail11", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.protectiveDesign)
                args.putString("detail22", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.softInteriorLining)
                args.putString("detail33", productList?.filterIsInstance<Accessory>() ?.find { it.productName == product.productName }?.productDetails?.includesCleaningCloth)

                args.putString("detail1", "Design")
                args.putString("detail2", "Feel")
                args.putString("detail3", "Includes")
            }

        }


        if(product.identifier == "eyeglasses") {
            productList?.filterIsInstance<Eyeglass>() ?.find { it.productName == product.productName }?.specifications?.let { args.putBoolean("Speci", it.prescription) }
            args.putString("uvProtection", productList?.filterIsInstance<Eyeglass>() ?.find { it.productName == product.productName }?.specifications?.uvProtection.toString())
            args.putString("antiReflectiveCoating", productList?.filterIsInstance<Eyeglass>() ?.find { it.productName == product.productName }?.specifications?.antiReflectiveCoating.toString())

        }


        else if(product.identifier == "sunglasses") {
            productList?.filterIsInstance<Sunglass>() ?.find { it.productName == product.productName }?.specifications?.let { args.putBoolean("Speci", it.prescription)}
            args.putString("uvProtection", productList?.filterIsInstance<Sunglass>() ?.find { it.productName == product.productName }?.specifications?.uvProtection.toString())
            args.putString("antiReflectiveCoating", productList?.filterIsInstance<Sunglass>() ?.find { it.productName == product.productName }?.specifications?.antiReflectiveCoating.toString())

        }
















        fragment.arguments = args

        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, fragment)
            .addToBackStack(null)
            .commit()
    }


}

