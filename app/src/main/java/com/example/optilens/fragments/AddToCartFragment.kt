package com.example.optilens.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.optilens.R
import com.example.optilens.activities.CheckoutActivity
import com.example.optilens.activities.HomePageActivity
import com.example.optilens.adapter.ItemInCartAdapter
import com.example.optilens.dataclass.CartItem
import com.example.optilens.dataclass.Product
import com.example.optilens.dataclass.ProductCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader

class AddToCartFragment : Fragment(){


    private lateinit var productCategory: ProductCategory
    private lateinit var recyclerView: RecyclerView
    private lateinit var amount: TextView
    private lateinit var continueShopping:TextView
    private lateinit var sc:ScrollView
    private lateinit var btn_Checkout : Button





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_to_cart, container, false)

        btn_Checkout = view.findViewById(R.id.btn_PlaceOrder)
        btn_Checkout.setOnClickListener(){
            val intent = Intent(activity , CheckoutActivity::class.java)
            startActivity(intent)
            //finish()
        }

        recyclerView = view.findViewById(R.id.recyclerViewAddtoCart)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        amount =view.findViewById(R.id.tv_Amount)
        sc = view.findViewById(R.id.sc_cartScrollView)
        continueShopping=view.findViewById(R.id.textViewNoItems)
        continueShopping.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
        productCategory = getProductCategoryFromJson(requireContext(), "products.json")

        getCartProductIds { cartProductIds ->
            val selectedProducts = getProductsByIds(cartProductIds, productCategory)
            val textViewNoItems = view.findViewById<TextView>(R.id.textViewNoItems)
            if(cartProductIds.isEmpty()) {
                textViewNoItems.visibility = View.VISIBLE
                sc.visibility = View.GONE
            }
            textViewNoItems.visibility = if (cartProductIds.isEmpty()) View.VISIBLE else View.GONE
            val animation = view.findViewById<ImageView>(R.id.iv_animation)
            animation.visibility = if (cartProductIds.isEmpty()) View.VISIBLE else View.GONE

            Glide.with(this)
                .load(R.drawable.cat)
                .into(DrawableImageViewTarget(animation))




            val adapter = ItemInCartAdapter(requireContext(), selectedProducts)
            recyclerView.adapter = adapter

        }



        getCartPrice { cartPriceList->
            if (cartPriceList.isEmpty()){
                amount.text="0"

            }else{
                val sumOfPrices = cartPriceList.sum()
                val formattedSum = String.format("%.2f", sumOfPrices)

                amount.text=formattedSum
            }

        }


        return view
    }
    private fun getUserRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        return database.reference.child("Users").child(userId)
    }
    private fun getCartProductIds(callback: (List<String>) -> Unit) {
        val userRef = getUserRef()

        userRef.child("cart").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                reloadFragment()
                val productIds = mutableListOf<String>()

                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CartItem::class.java)
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
    private fun getCartPrice(callback: (List<Double>) -> Unit) {
        val userRef = getUserRef()

        userRef.child("cart").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val priceList = mutableListOf<Double>()

                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CartItem::class.java)
                        item?.price?.let {
                            priceList.add(it)

                        }
                    }
                }




                callback(priceList)
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



}