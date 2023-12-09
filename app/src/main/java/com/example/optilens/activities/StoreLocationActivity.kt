package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.optilens.R
import com.example.optilens.fragments.HomeFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StoreLocationActivity : AppCompatActivity() , OnMapReadyCallback {
    private var mGoogleMap:GoogleMap?=null
    private lateinit var txtHome : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_location)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        txtHome = findViewById(R.id.tv_toolbar_title)

        txtHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap =googleMap

        val newyork = LatLng(40.71, -74.00)
        mGoogleMap!!.addMarker(MarkerOptions().position(newyork).title("Optilens NYC"))
        val syracuse = LatLng(43.04, -76.00)
        mGoogleMap!!.addMarker(MarkerOptions().position(syracuse).title("Optilens Syracuse"))
        val buffalo = LatLng(42.81, -78.87)
        mGoogleMap!!.addMarker(MarkerOptions().position(buffalo).title("Optilens Buffalo"))
        val mountainview = LatLng(37.38, -122.08)
        mGoogleMap!!.addMarker(MarkerOptions().position(mountainview).title("Optilens Headuarters"))
        val albany = LatLng(42.61, -73.75)
        mGoogleMap!!.addMarker(MarkerOptions().position(albany).title("Optilens Albany"))
        val dallas = LatLng(32.77, -96.79)
        mGoogleMap!!.addMarker(MarkerOptions().position(dallas).title("Optilens Dallas"))
        val boston = LatLng(42.71, -71.00)
        mGoogleMap!!.addMarker(MarkerOptions().position(boston).title("Optilens Boston"))
        val lasvegas = LatLng(36.5, -115.00)
        mGoogleMap!!.addMarker(MarkerOptions().position(lasvegas).title("Optilens Las Vegas"))
        val saltlake = LatLng(40.46, -111.58)
        mGoogleMap!!.addMarker(MarkerOptions().position(saltlake).title("Optilens Salt Lake City"))
        val seattle = LatLng(47.39, -122.00)
        mGoogleMap!!.addMarker(MarkerOptions().position(seattle).title("Optilens Seattle"))
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork , 4f))

        // Enable Zoom Controls
        val uiSettings = mGoogleMap!!.uiSettings
        uiSettings.isZoomControlsEnabled = true
    }
}