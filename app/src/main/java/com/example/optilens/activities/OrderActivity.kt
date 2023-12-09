package com.example.optilens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.optilens.R
import com.example.optilens.dataclass.Order
import com.example.optilens.dataclass.User
import com.example.optilens.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class OrderActivity : AppCompatActivity() {

    //private lateinit var binding : ActivityOrderActivityBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: DatabaseReference

    private lateinit var txt_OrderAmt: TextView
    private lateinit var txt_ProductName: TextView
    private lateinit var txt_OrderID : TextView
    private lateinit var txt_OrderStatus : TextView
    private lateinit var optiLens:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        databaseReference = FirebaseDatabase.getInstance().reference
        database= FirebaseDatabase.getInstance().reference

        //fetchDataFromFirebase()


        val imageView: ImageView = findViewById(R.id.imageView2)
        txt_OrderAmt = findViewById(R.id.txt_OrderTotal1)
        txt_ProductName = findViewById(R.id.txt_ProductName)
        txt_OrderID = findViewById(R.id.txt_OrderId2)
        txt_OrderStatus = findViewById(R.id.txt_Status1)
        optiLens= findViewById(R.id.toolbar_title_text)

        //  pricee = txt_OrderAmt.text.toString().toDoubleOrNull()


        // Load the animation from the XML file
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale)

        // Set the animation to the ImageView
        imageView.startAnimation(animation)

        displayCartItems()
        optiLens.setOnClickListener{
            deleteCart()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

    }


    private fun displayCartItems() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = databaseReference.child("Users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)

                    user?.let {
                        val cartItems = it.cart
                        val cartInfo = StringBuilder()
                        var totalCartValue = 0.0

                        for (cartItem in cartItems) {
                            cartInfo.append(
                                "Product ID: ${cartItem.productId}\n" +
                                        "Product Name: ${cartItem.productName}\n" +
                                        "Price: ${cartItem.price}\n"
                            )
                        }

                        for (cartItem in cartItems) {
                            cartItem.price?.let { price ->
                                totalCartValue += price
                            }
                        }

                        txt_OrderAmt.text = totalCartValue.toString()
                        txt_ProductName.text = cartInfo.toString()

                        val orderId = generateOrderId(userId) // Generate the order ID
                        val order = Order(
                            orderId = orderId,
                            products = it.cart,
                            totalAmount = totalCartValue,
                            orderDate = System.currentTimeMillis(), // Timestamp of the order date
                            status = "Pending" // Set the order status (e.g., "Pending")
                        )

                        txt_OrderID.text = orderId
                        txt_OrderStatus.text = order.status

                        // Fetch the existing orders to avoid overwriting them
                        userRef.child("orders").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentOrders = mutableListOf<Order>()

                                if (snapshot.exists()) {
                                    for (childSnapshot in snapshot.children) {
                                        val existingOrder = childSnapshot.getValue(Order::class.java)
                                        if (existingOrder != null) {
                                            currentOrders.add(existingOrder)
                                        }
                                    }
                                }

                                // Add the new order to the list of orders
                                currentOrders.add(order)

                                // Update the "orders" node in the Realtime Database
                                userRef.child("orders").setValue(currentOrders)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Order successfully placed
                                        } else {
                                            // Failed to place the order
                                        }
                                    }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle the error
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }



    private fun generateOrderId(userId: String): String {
        // Implement your logic to generate an order ID
        return "${userId}_${UUID.randomUUID()}"
    }



    private fun deleteCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = databaseReference.child("Users").child(userId).child("cart")

            userRef.removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Cart deleted successfully
                        // You can add any additional actions you want to perform after deleting the cart
                    } else {
                        // Failed to delete the cart
                        // Handle the error accordingly
                    }
                }
        }
    }



}

