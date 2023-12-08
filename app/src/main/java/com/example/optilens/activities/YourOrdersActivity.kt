package com.example.optilens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.optilens.R
import com.example.optilens.dataclass.Order
import com.example.optilens.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YourOrdersActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_orders)
        database = FirebaseDatabase.getInstance().getReference("orders")
        // loadOrders()
    }
}

//    private fun loadOrders(){
//
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            val userRef = database.child("Users").child(userId)
//
//            userRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//
//
//
//                    if (snapshot.exists()) {
//                        val user = snapshot.getValue(User::class.java)
//                        user?.let {
//                            val orderItems = it.orders
//                            val orderInfo = StringBuilder()
//                            for (orderItem in orderItems) {
//                                orderInfo.append(
//                                    "Product ID: ${or.productId}\n" +
//                                            "Product Name: ${cartItem.productName}\n" +
//                                            "Price: ${cartItem.price}\n"
//                                )
//                        }
//
//                        txtName.text = "Name : "+ user.name
//                        txtAddress.text = "Address : "+ user.address
//                        txtEmail.text ="Address : "+ user.email
//                        txtPhone.text="Phone Number : "+user.phoneNumber
//                        txtPassword.text="Password : "+user.password
//
//                    }
//                }
//                override fun onCancelled(databaseError: DatabaseError) {
//
//                }
//
//                })
//        }
//    }
//}
//
//
//
