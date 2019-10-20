package com.example.appchat.ListFriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.Model.User
import com.example.appchat.R
import com.example.messenger.ListFriends.ListFriendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_friend.*
import java.util.ArrayList

class ListFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_friend)
        rcv_of_listfiend.layoutManager= LinearLayoutManager(this)
        rcv_of_listfiend.setHasFixedSize(true)
        fetchData()

    }
    fun fetchData() :ArrayList<User>{
        var listFriend_FetchData =ArrayList<User>()
        val ref= FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach(){
                    val user= it.getValue(User::class.java)
                    val userCurrency= FirebaseAuth.getInstance().currentUser?.uid
                    if(user!=null&&!user.id.equals(userCurrency)){
                        listFriend_FetchData.add(user)
                    }
                }
                rcv_of_listfiend.adapter= ListFriendAdapter(this@ListFriendActivity,listFriend_FetchData)
            }

        })
        return  listFriend_FetchData;
    }
}
