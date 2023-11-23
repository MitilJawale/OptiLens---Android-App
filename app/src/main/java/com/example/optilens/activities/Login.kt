package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import com.example.optilens.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var loginPicture: ImageView
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnRegister: MaterialButton
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize loginPicture after setContentView
        loginPicture = findViewById(R.id.img_login)
        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister=findViewById(R.id.btn_register)
        auth = Firebase.auth


//        val imageResource = resources.getIdentifier("login_form_img", "drawable", packageName)
        val imageResource = R.drawable.login_form_img

        loginPicture.setImageResource(imageResource)

        btnLogin.setOnClickListener {
            val email: String = etEmail.text.toString()
            val password: String = etPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show()
            }
            else {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomePageActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }


        }
        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }
}