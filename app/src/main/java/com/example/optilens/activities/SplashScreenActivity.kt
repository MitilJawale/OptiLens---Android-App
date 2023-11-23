package com.example.optilens.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.optilens.R

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        Glide.with(this)
            .load(R.raw.splash_screen_specs)
            .into(DrawableImageViewTarget(gifImageView))

        Handler().postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 1710)


        // Textview
        val textView = findViewById<TextView>(R.id.tv_App_Name)

        // Calculate the width of the screen
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()

        // Create an ObjectAnimator for translation
        val animator = ObjectAnimator.ofFloat(
            textView,
            "translationX",
            -screenWidth,  // Start from the left of the screen
            0f            // Move to the original position
        )

        // Set animation duration
        animator.duration = 1000  // Adjust the duration as needed

        // Start the animation
        animator.start()




    }


}
