package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.optilens.R
import com.example.optilens.fragments.HomeFragment
import com.example.optilens.fragments.LensPowerFragment
import com.example.optilens.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvToolbarTitle: TextView
    private lateinit var btnpower : Button
    private lateinit var btnLogout: Button
    private lateinit var btnProfile : Button
    private lateinit var toolbar : Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Find all the views in your layout
        toolbar = findViewById(R.id.toolbar)
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title)


        // On clicking "Optilens" in toolbar user to be returned to homepage
        tvToolbarTitle.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }



        auth = Firebase.auth
        btnLogout = findViewById(R.id.btn_logout)
        btnpower = findViewById(R.id.btn_lens_power)
        btnProfile = findViewById(R.id.btn_profile)

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





        btnpower.setOnClickListener(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, LensPowerFragment())
                .addToBackStack(null)
                .commit()
        }

        btnProfile.setOnClickListener(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, ProfileFragment())
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



}