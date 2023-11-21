package com.example.optilens.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.optilens.R

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Find the Toolbar in your layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Set the Toolbar as the app bar for the activity
        setSupportActionBar(toolbar)

        // Avoids the Regular Project Name from displaying on the top
        supportActionBar?.setDisplayShowTitleEnabled(false)



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