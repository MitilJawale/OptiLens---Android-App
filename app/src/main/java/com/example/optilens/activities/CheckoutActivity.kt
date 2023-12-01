package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.optilens.R

class CheckoutActivity : AppCompatActivity() {


    private lateinit var btn_pay : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)


        btn_pay = findViewById(R.id.btnpay)


        btn_pay.setOnClickListener(){
            val intent = Intent(this , PaymentPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}