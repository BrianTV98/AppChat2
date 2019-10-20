package com.example.messenger.utils

import android.util.Log
import com.example.appchat.Model.User
import com.google.firebase.database.*
import java.util.ArrayList

class LoadDataCurrency {
     companion object{
         fun LoadDataDialog(){
             val ref= FirebaseDatabase.getInstance().getReference("/users/B88PVqJJtUOUO0DydDGVybYQPPU2")
             ref.addChildEventListener(object : ChildEventListener{
                 override fun onCancelled(p0: DatabaseError) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                 }

                 override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                     val user = p0.getValue(User::class.java)
                     Log.d("valueOfDialog", user?.getListDialog()?.get(0))
                 }

                 override fun onChildRemoved(p0: DataSnapshot) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

             })
         }
     }
}