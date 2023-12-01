package com.example.optilens.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.optilens.R
import com.example.optilens.adapter.ItemInWishlistAdapter
import com.example.optilens.dataclass.Product
import com.example.optilens.dataclass.ProductCategory
import com.example.optilens.dataclass.WishlistItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class WishListFragment : Fragment() {


    private lateinit var productCategory: ProductCategory
    private lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_wish_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewWishlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productCategory = getProductCategoryFromJson(requireContext(), "products.json")

        getWishlistProductIds { wishlistProductIds ->
            val selectedProducts = getProductsByIds(wishlistProductIds, productCategory)

            val adapter = ItemInWishlistAdapter(requireContext(), selectedProducts)
            recyclerView.adapter = adapter
        }


        return view
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
                            productIds.add(it)

                        }
                    }
                }


                callback(productIds)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("ProductAdapter", "Database error", error.toException())
            }
        })
    }
    fun readAndParseJsonFile(context: Context, fileName: String): ProductCategory? {
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                jsonString.append(line)
            }

            return Gson().fromJson(jsonString.toString(), ProductCategory::class.java)
        } catch (e: Exception) {
            Log.e("ProductListFragment", "Error reading JSON file", e)
        }

        return null
    }
    private fun getProductsByIds(productIds: List<String>, productCategory: ProductCategory): List<Product> {
        val allProducts = mutableListOf<Product>()

        allProducts.addAll(productCategory.eyeglasses)
        allProducts.addAll(productCategory.sunglasses)
        allProducts.addAll(productCategory.contactlens)
        allProducts.addAll(productCategory.accessories)

        return allProducts.filter { productIds.contains(it.productId) }
    }
    // Function to get all product IDs from the ProductCategory using JSON

    private fun getProductCategoryFromJson(context: Context, fileName: String): ProductCategory {
        val productList = readAndParseJsonFile(context, fileName)
        return productList ?: ProductCategory(emptyList(), emptyList(), emptyList(), emptyList())
    }

//    private fun getProductIdsFromJson(context: Context, fileName: String): List<String> {
//        val productList = readAndParseJsonFile(context, fileName)
//        val productIds = mutableListOf<String>()
//
//        productList?.eyeglasses?.forEach { productIds.add(it.productId) }
//        productList?.sunglasses?.forEach { productIds.add(it.productId) }
//        productList?.contactlens?.forEach { productIds.add(it.productId) }
//        productList?.accessories?.forEach { productIds.add(it.productId) }
//
//        return productIds
//    }

}