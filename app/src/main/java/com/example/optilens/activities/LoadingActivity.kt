package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.optilens.R
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.view.View


class LoadingActivity : AppCompatActivity() {

    private val DELAY_TIME_MS: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        FullScreencall();

        Handler().postDelayed({
            // Start the new activity after the delay
            val intent = Intent(this@LoadingActivity, OrderActivity::class.java) // Replace NewActivity with your target activity
            startActivity(intent)
            finish() // Optional: close the current activity
        }, DELAY_TIME_MS)


    }


    fun FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}