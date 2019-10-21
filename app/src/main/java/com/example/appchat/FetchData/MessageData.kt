package com.example.appchat.FetchData

import android.util.Log
import com.example.appchat.Dialog.DialogActivity
import com.example.appchat.Model.Message
import com.google.firebase.database.*
import com.stfalcon.chatkit.messages.MessagesListAdapter

class MessageData {
    fun getMessageHistory(idMessage: String, messagesListAdapter: MessagesListAdapter<Message>){
        val ref= FirebaseDatabase.getInstance().getReference("/message/${idMessage}")
        Log.d("abcdef",idMessage)
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var message= Message()
                p0.children.forEach(){

                    message= it.getValue(Message::class.java)!!
                    messagesListAdapter.addToStart(message, false)
                    Log.d("testUser",message.user.name)
                    Log.d("testdata", message.text)
                    if (message.imageUrl!=null){
                        Log.d("test" , message.imageUrl)
                    }
                    else Log.d("test" , "Khong co anh")
                }
            }

        })
    }
    fun getMessageRealtime(idMessage: String, messagesListAdapter: MessagesListAdapter<Message>){
        val ref= FirebaseDatabase.getInstance().getReference("/message/${idMessage}")
        val message =ref.limitToLast(1).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach(){
                    var message= it.getValue(Message::class.java)!!
                    message.imageUrl
                    Log.d("lassMessage",message.text)
                    Log.d("testUser",message.user.name)
                    if(message.user!= DialogActivity.userCurrently)
                    messagesListAdapter.addToStart(message, true)
                }
            }
        })
    }
}