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
import android.widget.Toast
import com.example.optilens.R
import com.example.optilens.dataclass.LensPrescription
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class LensPowerFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var btnSave: Button
    private lateinit var lsph: EditText
    private lateinit var rsph: EditText
    private lateinit var lcyl: EditText
    private lateinit var rcyl: EditText
    private lateinit var laxis: EditText
    private lateinit var raxis: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_lens_power, container, false)
        btnSave = view.findViewById(R.id.savebutton)
        lsph = view.findViewById(R.id.leftsph)
        rsph = view.findViewById(R.id.rightsph)
        lcyl = view.findViewById(R.id.leftcly)
        rcyl = view.findViewById(R.id.rightcly)
        laxis = view.findViewById(R.id.leftaxis)
        raxis = view.findViewById(R.id.rightaxis)
        auth = Firebase.auth

        database = Firebase.database.reference

        btnSave.setOnClickListener{
            saveLensPower()
        }


        return view
    }

    private fun saveLensPower(){
        //getting values
        val ls = lsph.text.toString()
        val rs = rsph.text.toString()
        val lc = lcyl.text.toString()
        val rc = rcyl.text.toString()
        val la = laxis.text.toString()
        val ra = raxis.text.toString()

        if(ls.isEmpty()){
            lsph.error = "Please Enter Left Spherical Value"
        }
        if(rs.isEmpty()){
            rsph.error = "Please Enter Right Spherical Value"
        }
        if(lc.isEmpty()){
            lcyl.error = "Please Enter Left Cylindrical Value"
        }
        if(rc.isEmpty()){
            rcyl.error = "Please Enter Right Cylindrical Value"
        }
        if(la.isEmpty()){
            laxis.error = "Please Enter Left Axis Value"
        }
        if(ra.isEmpty()){
            raxis.error = "Please Enter Right Axis Value"
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("currentUser",currentUser.toString())

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child("Users").child(userId)
            var lens = LensPrescription( rs,rc,ra,ls,lc,la)

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

//
//package com.example.optilens.fragments
//
//import android.os.Bundle
//import com.google.firebase.database.DatabaseReference
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import com.example.optilens.R
//import com.example.optilens.dataclass.LensPrescription
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//import com.google.firebase.database.FirebaseDatabase
//
//class LensPowerFragment : Fragment() {
//
//    private lateinit var databaseReference: DatabaseReference
//    private lateinit var btnSave: Button
//    private lateinit var lsph: EditText
//    private lateinit var rsph: EditText
//    private lateinit var lcyl: EditText
//    private lateinit var rcyl: EditText
//    private lateinit var laxis: EditText
//    private lateinit var raxis: EditText
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view: View = inflater.inflate(R.layout.fragment_lens_power, container, false)
//        btnSave = view.findViewById(R.id.savebutton)
//        lsph = view.findViewById(R.id.leftsph)
//        rsph = view.findViewById(R.id.rightsph)
//        lcyl = view.findViewById(R.id.leftcly)
//        rcyl = view.findViewById(R.id.rightcly)
//        laxis = view.findViewById(R.id.leftaxis)
//        raxis = view.findViewById(R.id.rightaxis)
//        auth = Firebase.auth
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//
//        btnSave.setOnClickListener{
//            saveLensPower()
//        }
//
//
//        return view
//    }
//
//    private fun saveLensPower(){
//        //getting values
//        val ls = lsph.text.toString()
//        val rs = rsph.text.toString()
//        val lc = lcyl.text.toString()
//        val rc = rcyl.text.toString()
//        val la = laxis.text.toString()
//        val ra = raxis.text.toString()
//
//        if(ls.isEmpty()){
//            lsph.error = "Please Enter Left Spherical Value"
//        }
//        if(rs.isEmpty()){
//            rsph.error = "Please Enter Right Spherical Value"
//        }
//        if(lc.isEmpty()){
//            lcyl.error = "Please Enter Left Cylindrical Value"
//        }
//        if(rc.isEmpty()){
//            rcyl.error = "Please Enter Right Cylindrical Value"
//        }
//        if(la.isEmpty()){
//            laxis.error = "Please Enter Left Axis Value"
//        }
//        if(ra.isEmpty()){
//            raxis.error = "Please Enter Right Axis Value"
//        }
//
//
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            val lens = LensPrescription(userId , rs , rc , ra,ls,lc,la  )
//            // var lens = LensPrescription(userId , cylLeft  ,cylRight = null , sphLeft = null, axisRight = null, axisLeft = null, sphRight = null)
//            databaseReference.child(userId).child("lensPrescription").setValue(lens)
//
//        } else {
//            // The user is not signed in
//        }
//
////
////        val lens = LensPrescription(userId , rs , rc , ra,ls,lc,la  )
////        // var lens = LensPrescription(userId , cylLeft  ,cylRight = null , sphLeft = null, axisRight = null, axisLeft = null, sphRight = null)
////        databaseReference.child(userId).child("lensPrescription").setValue(lens)
////            .addOnCompleteListener{
////                Toast.makeText(this , "Data InsertedSuccessfully" , Toast.LENGTH_LONG).show()
////            }.addOnFailureListener{
//
//    }
//
//
//
//
//}