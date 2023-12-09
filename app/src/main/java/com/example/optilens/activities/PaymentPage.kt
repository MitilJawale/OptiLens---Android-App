package com.example.optilens.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import com.example.optilens.R
import com.example.optilens.fragments.AddToCartFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PaymentPage : AppCompatActivity() {

    private lateinit var cardForm: CardForm
    private lateinit var btnBuy: Button
    private lateinit var btnBack: ImageButton
    private lateinit var tvToolbarTitleText: TextView
    private lateinit var btnLogOut: Button
    private val DELAY_TIME_MS: Long = 3000



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)


        tvToolbarTitleText = findViewById(R.id.toolbar_title_text)
        tvToolbarTitleText.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        btnLogOut = findViewById(R.id.btn_logout)
        btnLogOut.setOnClickListener(){
            Firebase.auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }



        cardForm = findViewById(R.id.cardForm)
        btnBuy = findViewById<Button>(R.id.btnBuy)
//        btnBack = findViewById<ImageButton>(R.id.btn_back)
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .actionLabel("Purchase")
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("Mobile Number is required for SMS verification")
            .setup(this@PaymentPage)

        cardForm.cvvEditText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD

//        btnBuy.setOnClickListener() {
//            if (cardForm.isValid) {
//                Toast.makeText(
//                    this@PaymentPage, "Payment Done", Toast.LENGTH_LONG
//                ).show()
//            } else {
//                Toast.makeText(this@PaymentPage, "Please fill all details", Toast.LENGTH_LONG)
//                    .show()
//            }
       // }

        btnBuy.setOnClickListener() {

            if (cardForm.isValid)
            {
                val intent = Intent(this , LoadingActivity::class.java)
                startActivity(intent)

                finish()

            }
            else {
               Toast.makeText(this@PaymentPage, "Please fill all details", Toast.LENGTH_LONG)
                    .show()
            }



//        btnBack.setOnClickListener() {
//
//            val intent = Intent(this , CheckoutActivity::class.java)
//            startActivity(intent)
//
//            finish()
//        }
    }

}}