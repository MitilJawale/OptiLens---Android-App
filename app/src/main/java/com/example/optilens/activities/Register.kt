package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.optilens.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Register : AppCompatActivity() {
    private lateinit var etName: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var btnBackLogin: MaterialButton
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        etName = findViewById(R.id.name)
        etAddress = findViewById(R.id.address)
        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.password)
        btnRegister = findViewById(R.id.btn_register)
        btnBackLogin = findViewById(R.id.btn_back_login)
        auth = Firebase.auth


        btnRegister.setOnClickListener {
            val name: String = etName.text.toString()
            val address: String = etAddress.text.toString()
            val email: String = etEmail.text.toString()
            val password: String = etPassword.text.toString()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Address field is empty", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            }


            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext,"Registration Successful",Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(baseContext,"Authentication failed",Toast.LENGTH_SHORT).show()
                        }
                    }
            }



        }


        btnBackLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}