package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.optilens.R

class SampleCartActivity : AppCompatActivity() {

    private lateinit var btn_Checkout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_cart)

        btn_Checkout = findViewById(R.id.btnCheckout)


        btn_Checkout.setOnClickListener(){
            val intent = Intent(this , CheckoutActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}