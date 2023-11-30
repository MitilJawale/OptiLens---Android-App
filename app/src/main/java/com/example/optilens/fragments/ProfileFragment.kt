package com.example.optilens.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.optilens.R
import com.example.optilens.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {
    private lateinit var selectImage: Button
    private lateinit var previewImage: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtPassword: TextView



    private lateinit var database: DatabaseReference

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        selectImage = view.findViewById(R.id.btn_selectImage)
        previewImage = view.findViewById(R.id.IVPreviewImage)
        txtName = view.findViewById(R.id.txt_Name)
        txtAddress = view.findViewById(R.id.txt_Address)
        txtEmail = view.findViewById(R.id.txt_Email)
        txtPhone = view.findViewById(R.id.txt_Number)
        txtPassword = view.findViewById(R.id.txt_Password)
        database = FirebaseDatabase.getInstance().reference


//        previewImage.setImageResource(R.drawable.boy_profile_picture)

        // Load user data and display
        loadUserData()

        selectImage.setOnClickListener {
            chooseImage()
        }

        return view
    }

    private fun loadUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child("Users").child(userId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)

                        if (user != null) {
                            // Display user information
                            txtName.text = "Name : "+ user.name
                            txtAddress.text = "Address : "+ user.address
                            txtEmail.text ="Address : "+ user.email
                            txtPhone.text="Phone Number : "+user.phoneNumber
                            txtPassword.text="Password : "+user.password
                            if (user.profilePicture != " ") {
                                // Load and display profile picture using Glide
                                Glide.with(requireContext())
                                    .load(user.profilePicture)
                                    .into(previewImage)
                            } else {
                                // If no profile picture is available, you might want to set a default image
                                previewImage.setImageResource(R.drawable.boy_profile_picture)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!

            Log.d("ImageUri", imageUri.toString())

            previewImage.setImageURI(imageUri)
            uploadImageToFirebaseStorage(imageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val userImagesRef: StorageReference =
            storageRef.child("User_Images/${FirebaseAuth.getInstance().currentUser?.uid}")

        userImagesRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                userImagesRef.downloadUrl.addOnSuccessListener { uri ->
                    updateProfilePicture(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    private fun updateProfilePicture(profilePictureUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child("Users").child(userId)

            userRef.child("profilePicture").setValue(profilePictureUrl)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile picture updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Failure to update Picture",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}