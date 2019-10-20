package com.example.appchat.FetchData

import com.example.appchat.Model.Dialogs
import com.example.appchat.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stfalcon.chatkit.dialogs.DialogsListAdapter

class DialogData {
    fun getDataDialogOfUser(user: User, dialogListAdapter: DialogsListAdapter<Dialogs>){
        val ref= FirebaseDatabase.getInstance().getReference("/dialogs")
        val list = user.getListDialog()
        var listDialogs = ArrayList<Dialogs>()
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                list.forEach {
                    var dialogs= p0.child(it).getValue(Dialogs::class.java)
                    if(dialogs!=null)  listDialogs.add(dialogs)
                }
                dialogListAdapter.setItems(listDialogs)
            }

        })
    }
}