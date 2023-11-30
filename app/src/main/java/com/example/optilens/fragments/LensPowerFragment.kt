package com.example.optilens.fragments

import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.optilens.R
import com.example.optilens.dataclass.LensPrescription
import com.example.optilens.dataclass.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LensPowerFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var btnSave: Button
    private lateinit var display_ls: TextView
    private lateinit var display_lc: TextView
    private lateinit var display_la: TextView

    private lateinit var display_rs: TextView
    private lateinit var display_rc: TextView
    private lateinit var display_ra: TextView


    private lateinit var lsph: EditText
    private lateinit var rsph: EditText
    private lateinit var lcyl: EditText
    private lateinit var rcyl: EditText
    private lateinit var laxis: EditText
    private lateinit var raxis: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseref: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_lens_power, container, false)
        btnSave = view.findViewById(R.id.savebutton)
        display_ls = view.findViewById(R.id.row1_col2)
        display_lc = view.findViewById(R.id.row1_col3)
        display_la = view.findViewById(R.id.row1_col4)

        display_rs = view.findViewById(R.id.row2_col2)
        display_rc = view.findViewById(R.id.row2_col3)
        display_ra = view.findViewById(R.id.row2_col4)

        lsph = view.findViewById(R.id.leftsph)
        rsph = view.findViewById(R.id.rightsph)
        lcyl = view.findViewById(R.id.leftcly)
        rcyl = view.findViewById(R.id.rightcly)
        laxis = view.findViewById(R.id.leftaxis)
        raxis = view.findViewById(R.id.rightaxis)
        auth = Firebase.auth

        database = Firebase.database.reference
        databaseref = FirebaseDatabase.getInstance().reference

        btnSave.setOnClickListener{
            saveLensPower()
        }
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = databaseref.child("Users").child(userId).child("lensPrescription") // Ensure correct case

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lensPrescription = snapshot.getValue(LensPrescription::class.java)

                        if (lensPrescription != null) {
                            display_ls.text = lensPrescription.sphLeft ?: ""
                            display_lc.text = lensPrescription.cylLeft ?: ""
                            display_la.text = lensPrescription.axisLeft ?: ""

                            display_rs.text = lensPrescription.sphRight ?: ""
                            display_rc.text = lensPrescription.cylRight ?: ""
                            display_ra.text = lensPrescription.axisRight ?: ""
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }


        return view
    }

    private fun saveLensPower(){
        //getting values
        val lefts = lsph.text.toString()
        val rights = rsph.text.toString()
        val leftc = lcyl.text.toString()
        val rightc = rcyl.text.toString()
        val lefta = laxis.text.toString()
        val righta = raxis.text.toString()

        val ls = lsph.text.toString().toDoubleOrNull()
        val rs = rsph.text.toString().toDoubleOrNull()
        val lc = lcyl.text.toString().toDoubleOrNull()
        val rc = rcyl.text.toString().toDoubleOrNull()
        val la = laxis.text.toString().toDoubleOrNull()
        val ra = raxis.text.toString().toDoubleOrNull()

        if (ls == null || ls !in -20.0..20.0) {
            lsph.error = "Please Enter a valid Left Spherical Value between -20 and 20"
            return
        }

        if (rs == null || rs !in -20.0..20.0) {
            rsph.error = "Please Enter a valid Right Spherical Value between -20 and 20"
            return
        }

        if (lc == null || lc !in -4.0..4.0) {
            lcyl.error = "Please Enter a valid Left Cylindrical Value between -4 and 4"
            return
        }

        if (rc == null || rc !in -4.0..4.0) {
            rcyl.error = "Please Enter a valid Right Cylindrical Value between -4 and 4"
            return
        }

        if (la == null || la !in 0.0..180.0) {
            laxis.error = "Please Enter a valid Left Axis Value between 0 and 180"
            return
        }

        if (ra == null || ra !in 0.0..180.0) {
            raxis.error = "Please Enter a valid Right Axis Value between 0 and 180"
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("currentUser",currentUser.toString())

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child("Users").child(userId)
            var lens = LensPrescription( rights,rightc,righta,lefts,leftc,lefta)

            userRef.child("lensPrescription").setValue(lens)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Lens details updated", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failure to update Lens Details", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }

}
