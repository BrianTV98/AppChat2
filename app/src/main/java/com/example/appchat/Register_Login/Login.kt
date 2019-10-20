package com.example.appchat.Register_Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appchat.Dialog.DialogActivity
import com.example.appchat.Model.User
import com.example.appchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class Login : Fragment() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        perform()
        super.onActivityCreated(savedInstanceState)
    }

    private fun perform() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        Log.d("Login", "Email: ${email}")
        Log.d("Login", "Password: ${password}")

        login_btn_login.setOnClickListener {
            if (email_edittext_login.text.isEmpty() || password_edittext_login.text.isEmpty()) {
                Toast.makeText(
                    context,
                    "Tên đăng nhập hoặc mật khâu không được rỗng",
                    Toast.LENGTH_SHORT
                )
                    .show();

            } else {
                auth.signInWithEmailAndPassword(
                    email_edittext_login.text.toString(),
                    password_edittext_login.text.toString()
                )
                    .addOnSuccessListener {
                        gotoDialog(context)
                        Log.d("login", "Dang nhap  thanh cong")

                    }
                    .addOnFailureListener {
                        Log.d("login", "Dang nhap  that bai")
                        //                        Toast.makeText(this, "Dang nhap that bai",Toast.LENGTH_SHORT).show();
                        password_edittext_login.setText("")
                        email_edittext_login.setText("")
                        email_edittext_login.requestFocus()
                    }
            }
        }

    }

    private fun gotoDialog(context: Context?) {
        // lay  id user hien
        val uidCurrently = FirebaseAuth.getInstance().currentUser!!.uid
        // lay data user hien ta
        // put len bundel
        // gui qua intent
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.child(uidCurrently).getValue(User::class.java)
                val intent = Intent(context, DialogActivity::class.java)
                intent.putExtra("userCurrently", user)
                startActivity(intent)
            }
        })
    }


}
