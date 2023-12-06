package com.example.optilens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.optilens.R
import com.example.optilens.dataclass.Order
import com.example.optilens.dataclass.User
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
    private lateinit var txt_OrderAmt: TextView
    private lateinit var txt_ProductName: TextView
    private lateinit var txt_OrderID : TextView
    private lateinit var txt_OrderStatus : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        databaseReference = FirebaseDatabase.getInstance().reference
        //fetchDataFromFirebase()


        val imageView: ImageView = findViewById(R.id.imageView2)
        txt_OrderAmt = findViewById(R.id.txt_OrderTotal1)
        txt_ProductName = findViewById(R.id.txt_ProductName)
        txt_OrderID = findViewById(R.id.txt_OrderId2)
        txt_OrderStatus = findViewById(R.id.txt_Status1)

        //  pricee = txt_OrderAmt.text.toString().toDoubleOrNull()


        // Load the animation from the XML file
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale)

        // Set the animation to the ImageView
        imageView.startAnimation(animation)

        displayCartItems()

    }
//    private fun fetchDataFromFirebase() {
    // Assuming you have a "data" node in your Firebase database
//
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        //var pricee = txt_OrderAmt.text.toString().toDoubleOrNull()
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            val userRef = databaseReference.child("OptiLens").child(userId)
//
//            userRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        val dataValue = dataSnapshot.getValue(CartItem::class.java)
//
//                        if (dataValue != null) {
//                            val stringValue = dataValue?.toString()?.toDouble() ?: "N/A"
//
//                            // Set the value directly to the TextView using Kotlin Android Extensions
//                            txt_OrderAmt.text = stringValue.price
//                            //txt_OrderAmt = dataValue.price
//                        }
//
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Failed to read value
//                    // Log.d("FirebaseData", "Failed to read value: $error")
//                }
//            })
//
//        }
//    }
//}

//
//    private fun loadUserData() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            val userRef = databaseReference.child("User").child(userId)
//
//            userRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                        if (snapshot.exists()) {
//                            val user = snapshot.getValue(CartItem::class.java)
//
//                            if (user != null) {
//
//                                val orderAmount = user.price // Default value if 'price' is null
//                                txt_OrderAmt.text = orderAmount.toString()
//                                txt_ProductName.text = user.productName
//
//
//


    // txt_OrderAmt = user.price
//                                // Display user information
//                                txtName.text = "Name : "+ user.name
//                                txtAddress.text = "Address : "+ user.address
//                                txtEmail.text ="Address : "+ user.email
//                                txtPhone.text="Phone Number : "+user.phoneNumber
//                                txtPassword.text="Password : "+user.password
//                                if (user.profilePicture != " ") {
//                                    // Load and display profile picture using Glide
//                                    Glide.with(requireContext())
//                                        .load(user.profilePicture)
//                                        .into(previewImage)
//                                } else {
//                                    // If no profile picture is available, you might want to set a default image
//                                    previewImage.setImageResource(R.drawable.boy_profile_picture)
//                                }
//                            }
//                        }
//
//
//                    override fun onCancelled(error: DatabaseError) {
//                    // Handle the error
//                }
//            })
//        }
//    }
//}


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

                            txt_OrderAmt.text = totalCartValue.toString()
                            txt_ProductName.text = cartInfo.toString()
                        }


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

                        userRef.child("Orders").child(orderId).setValue(order)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Order successfully placed
                                } else {
                                    // Failed to place the order
                                }
                            }
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
    }