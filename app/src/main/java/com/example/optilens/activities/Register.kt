package com.example.optilens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.optilens.R
import com.example.optilens.dataclass.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Register : AppCompatActivity() {
    private lateinit var etName: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var btnBackLogin: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var selectedGender: String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        etName = findViewById(R.id.name)
        etAddress = findViewById(R.id.address)
        val gender = resources.getStringArray(R.array.Gender)
        etPhone = findViewById(R.id.phone)
        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.password)
        btnRegister = findViewById(R.id.btn_register)
        btnBackLogin = findViewById(R.id.btn_back_login)
        auth = Firebase.auth
        database = Firebase.database.reference




        //The spinner is for selecting gender
        val spinner = findViewById<Spinner>(R.id.Gender)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, gender)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    selectedGender = spinner.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@Register,"Select Gender", Toast.LENGTH_SHORT).show()                }
            }
        }



            btnRegister.setOnClickListener {
            val name: String = etName.text.toString()
            val address: String = etAddress.text.toString()
            val phone: String = etPhone.text.toString()
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
            } else if (phone.length < 10) {
                Toast.makeText(this, "Incorrect Phone", Toast.LENGTH_SHORT).show()
            }


            else {



                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext,"Registration Successful",Toast.LENGTH_SHORT).show()
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            if (currentUser != null) {
                                val userId = currentUser.uid
                                writeNewUser(userId, name, address, phone, email, password)
                            }

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



    private fun writeNewUser(userId: String, name: String, address: String, phone: String, email: String, password: String) {
//        val userId = database.push().key!!

        val user = User(name ,address,phone,email,password,selectedGender," ")

        database.child("Users").child(userId).setValue(user)
    }

}