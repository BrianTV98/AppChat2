package com.example.messenger.ListFriends

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.Dialog.DialogActivity
import com.example.appchat.Message.MessageActivity
import com.example.appchat.Model.Dialogs
import com.example.appchat.Model.User
import com.example.appchat.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_chat.view.*
import kotlin.collections.ArrayList

class ListFriendAdapter (val context: Context, var listFriend: ArrayList<User>) :RecyclerView.Adapter<ListFriendAdapter.ViewHolder>(){
    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFriendAdapter.ViewHolder {
      val view= LayoutInflater.from(context)
          .inflate(R.layout.layout_item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  listFriend.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUrl=listFriend[position].avatar
        val name=listFriend[position].name
        val id= listFriend[position].id
        Picasso.get().load(photoUrl).into(holder.itemView.avata_img_chatlog)
        holder.itemView.textView_listfiend.text= name

        holder.itemView.setOnClickListener {
            createDialoag(holder.itemView.context, photoUrl, listFriend[position])
        }
    }
    // tao ra dialog cho view, update listDiaglog cua cac user, va Create Dialog  in firebase
    private fun createDialoag(context: Context, photoUrl: String, friend: User) {

        // lay user dang nhap hien tai
        val userCurrency=  DialogActivity.userCurrently

        // id cua dialog = 2 id cong lai
        val idDialogs = userCurrency.id +friend.id

        // tao list User cho dialogs
        var listUser = ArrayList<User>()

        // tao listUser cho dialog
        listUser.add(userCurrency)
        listUser.add(friend)
        // tao ten cho dialog
        if(checkExistDialogs(userCurrency, friend)==false){
            val nameDialogs= userCurrency.name+","+friend.name


            // cap nhap dialog cho user
            updateDialogOfUser(userCurrency, idDialogs)
            updateDialogOfUser(friend, idDialogs)

            val dialogs= Dialogs(idDialogs,photoUrl,nameDialogs,listUser,null,listUser.size)

            createDialogOnFireBase(dialogs)
            DialogActivity.dialogsListAdapter.addItem(dialogs) //

            // chuyen Inten Mesage
            val intent=Intent(context, MessageActivity::class.java)
            intent.putExtra("idDialog", nameDialogs)
            Toast.makeText(context,nameDialogs,Toast.LENGTH_LONG).show()
            context.startActivity(intent)
        }

    }
    // tra ve true neu da ton tai dialog cua 2 user
    private fun checkExistDialogs(userCurrency: User, friend: User) :Boolean {
        val name1= userCurrency.id+ friend.id
        val name2= friend.id +userCurrency.id
        userCurrency.getListDialog().forEach {
            if(it.equals(name1)||it.equals(name2)) return true
        }
        return  false
    }

    private fun createDialogOnFireBase(dialogs: Dialogs) {
        val ref= FirebaseDatabase.getInstance().getReference("/dialogs/${dialogs.id}")
        ref.setValue(dialogs)

    }

    private fun updateDialogOfUser(user: User, idDialogs:String) {
        //update view
        val listDialogs=user.getListDialog()
        listDialogs.add(idDialogs)
        user.setListDialog(listDialogs)
        // update firebase
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.child(user.id).child("listDialog").setValue(user.getListDialog())
    }
}