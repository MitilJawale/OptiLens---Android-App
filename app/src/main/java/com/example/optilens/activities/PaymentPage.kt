package com.example.optilens.activities


import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import com.example.optilens.R



class PaymentPage : AppCompatActivity() {

    private lateinit var cardForm : CardForm
    private lateinit var btnBuy : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)


        cardForm = findViewById(R.id.cardForm)
        btnBuy = findViewById<Button>(R.id.btnBuy)
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .actionLabel("Purchase")
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("Mobile Number is required for SMS verification")
            .setup(this@PaymentPage)

        cardForm.cvvEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD

        btnBuy.setOnClickListener(){
            if(cardForm.isValid){
                Toast.makeText(this@PaymentPage, "Payment Done"
                    ,Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@PaymentPage,"Please fill all details",Toast.LENGTH_LONG).show()
            }
        }



    }
}