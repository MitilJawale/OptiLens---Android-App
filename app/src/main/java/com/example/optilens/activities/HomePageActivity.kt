package com.example.optilens.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.optilens.R
import com.example.optilens.fragments.HomeFragment
import com.example.optilens.fragments.LensPowerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var btnLogout: Button
    private lateinit var btnLensPower : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Find the Toolbar in your layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        auth = Firebase.auth
        btnLogout = findViewById(R.id.btn_logout)
        btnLensPower = findViewById(R.id.btn_lens_power)
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        btnLogout.setOnClickListener(){
            Firebase.auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        btnLensPower.setOnClickListener(){

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, LensPowerFragment())
                .addToBackStack(null)
                .commit()
            }


        // Set the Toolbar as the app bar for the activity
        setSupportActionBar(toolbar)

        // Avoids the Regular Project Name from displaying on the top
        supportActionBar?.setDisplayShowTitleEnabled(false)

        loadFragment(HomeFragment())

    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the existing fragment with the new one
        fragmentTransaction.replace(R.id.frame_layout_main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }









    // BELOW CODE CAN BE USED FOR ADDING FUNCTIONALITY TO TOOLBAR
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//
//    // Optionally, you can handle menu item clicks in onOptionsItemSelected
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            // Handle your menu item clicks here
//            R.id.action_settings -> {
//                // Do something
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


}