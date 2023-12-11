package com.example.optilens.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.optilens.R
import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var btn_pay: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnLocation: ImageButton
    private lateinit var txtAddress1: TextView
    private lateinit var txtAddress2: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtState: TextView
    private lateinit var txtZip: TextView
    private lateinit var txtCountry : TextView
    private lateinit var txtFName : EditText
    private lateinit var txtLName : EditText
    private lateinit var txtEmail : EditText
    private lateinit var txtNumber : EditText
    private lateinit var tvToolbarTitleText : TextView
    private lateinit var btnLogOut : Button
    private var PERMISSION_ID = 1010
    lateinit var locationRequest: LocationRequest
    private lateinit var database: DatabaseReference

    companion object {
        const val REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        btn_pay = findViewById(R.id.btnpay)
        btn_pay.setOnClickListener() {
            if (validateFields()) {
                val intent = Intent(this, PaymentPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
            }


        }

        txtAddress1 = findViewById(R.id.et_addressLine1)
        txtCity = findViewById(R.id.et_city)
        txtState = findViewById(R.id.et_state)
        txtZip = findViewById(R.id.et_zip)
        txtCountry = findViewById(R.id.et_country)
        btnLocation = findViewById(R.id.btnLocation)
        txtFName = findViewById(R.id.et_firstName)
        txtLName = findViewById(R.id.et_lastName)
        txtEmail = findViewById(R.id.et_emailAddress)
        txtNumber = findViewById(R.id.et_mobileNumber)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnLocation.setOnClickListener() {
            getlastlocation()
        }

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
    }

    private fun getlastlocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        try {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            txtAddress1.text = " ${addresses?.get(0)?.thoroughfare}"
                            txtCity.text = " ${addresses?.get(0)?.locality}"
                            txtZip.text = " ${addresses?.get(0)?.postalCode}"
                            txtState.text = "${addresses?.get(0)?.adminArea}"
                            txtCountry.text = "${addresses?.get(0)?.countryName}"
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlastlocation()
            } else {
                Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validateFields(): Boolean {
        val firstName = txtFName.text.toString()
        val lastName = txtLName.text.toString()
        val email = txtEmail.text.toString()
        val phoneNumber = txtNumber.text.toString()

        // Check if any of the fields is empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            return false
        }

        // Add additional validation checks if needed, e.g., for email format or phone number format

        return true
    }
}


