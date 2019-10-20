package com.example.appchat.Register_Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appchat.Dialog.DialogActivity
import com.example.appchat.MainActivity
import com.example.appchat.Model.User
import com.example.appchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class Register : Fragment() {
    var selectedPhotoUrl: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        register_btn_register.setOnClickListener {

            val userName = userName_edit_Register.text.toString()
            val email = email_editext_Register.text.toString()
            val password = password_edittext_Register.text.toString()
            perform(userName, email, password)
        }
        avata_button_register.setOnClickListener {
            Log.d("Register", "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // thu muc image
            startActivityForResult(intent, 0)
            // set avata

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Register", "Photo was selected")
            selectedPhotoUrl = data.data
        }
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
            MainActivity.contextOfApplication.contentResolver,
            selectedPhotoUrl
        )
        avata_button_register.setImageBitmap(bitmap)
    }

    //
    fun perform(userName: String, email: String, password: String) {

        Log.d("Register", "user: ${userName}")
        Log.d("Register", "email: ${email}")
        Log.d("Register", "password:$password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                MainActivity.contextOfApplication,
                "Please enter  text email and password",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val auth = FirebaseAuth.getInstance()
        if (selectedPhotoUrl != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(
                            "Register:",
                            " Register successed created user with uid${it.result?.user?.uid}"
                        )
                        uploadImageToFireBaseStorage(userName)
                    } else return@addOnCompleteListener
                }
                .addOnFailureListener() {
                    Log.d("Register", " Failed to created user : ${it.message}")
                    Toast.makeText(
                        MainActivity.contextOfApplication,
                        "Failed to created user : ${it.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
        } else Toast.makeText(
            MainActivity.contextOfApplication,
            "Vui long chon anh",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun uploadImageToFireBaseStorage(userName: String) {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$fileName")

        ref.putFile(selectedPhotoUrl!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully upload image ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register", " File location $it")
                    savaUserToFireBaseDataBase(it.toString(), userName)
                }
            }
    }

    //
    private fun savaUserToFireBaseDataBase(profileImage: String, userName: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            userName,
            profileImage,
            false,
            ArrayList()
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Finally we saved the user to Firebase Database")
                val intent = Intent(context, DialogActivity::class.java)
                intent.putExtra("userCurrently", user)
                startActivity(intent)
            }
    }


}



