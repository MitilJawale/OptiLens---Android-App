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
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import com.example.optilens.dataclass.LensPrescription
import com.example.optilens.dataclass.User
import com.google.android.gms.tasks.OnSuccessListener

import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
            val intent = Intent(this, PaymentPage::class.java)
            startActivity(intent)
            finish()
        }

        txtAddress1 = findViewById(R.id.btnAddress1)
        txtCity = findViewById(R.id.btnCity)
        txtState = findViewById(R.id.btnState)
        txtZip = findViewById(R.id.btnZip)
        txtCountry = findViewById(R.id.btnCountry)
        btnLocation = findViewById(R.id.btnLocation)
        txtFName = findViewById(R.id.btnFName)
        txtLName = findViewById(R.id.btnLName)
        txtEmail = findViewById(R.id.btnEmail)
        txtNumber = findViewById(R.id.btnTextPhone)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnLocation.setOnClickListener() {
            getlastlocation()
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
}


