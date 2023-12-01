package com.example.optilens.activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import com.example.optilens.R
import android.app.AlertDialog.Builder


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

//        btnBuy.setOnClickListener(){
//            if(cardForm.isValid){
//                Toast.makeText(this@PaymentPage, "Card Number: ${cardForm.cardNumber}" +
//                    "CardHolder Name : + ${cardForm.cardholderName}" +
//                    "Card cvv: + ${cardForm.cvv}" +
//                    "Card Expiry Date : + ${cardForm.expirationDateEditText.toString()}"+
//                    "Postal Code: + ${cardForm.postalCode}"+
//                    "Mobile Number + ${cardForm.mobileNumber}"
//                    ,Toast.LENGTH_LONG).show()
//            }
//            else{
//                Toast.makeText(this@PaymentPage,"Please fill all details",Toast.LENGTH_LONG).show()
//            }
//        }

        val builder = AlertDialog.Builder(this)

        //val alertBuilder: AlertDialog.Builder(this)
        builder.setTitle("Confirm before purchase")
        builder.setMessage(
            """
                            Card number: ${cardForm.cardNumber}
                            Card expiry date: ${cardForm.expirationDateEditText.text.toString()}
                            Card CVV: ${cardForm.cvv}
                            Postal code: ${cardForm.postalCode}
                            Phone number: ${cardForm.mobileNumber}
                            """.trimIndent()
        )
        builder.setPositiveButton("Confirm",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                Toast.makeText(this@PaymentPage, "Thank you for purchase", Toast.LENGTH_LONG)
                    .show()
            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()



    }
}