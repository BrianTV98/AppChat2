package com.example.appchat.Dialog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.FetchData.DialogData
import com.example.appchat.ListFriends.ListFriendActivity
import com.example.appchat.MainActivity
import com.example.appchat.Message.MessageActivity
import com.example.appchat.Model.Dialogs
import com.example.appchat.Model.User
import com.example.appchat.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.activity_dialogmain.*

class DialogActivity : AppCompatActivity(), DialogsListAdapter.OnDialogClickListener<Dialogs>,
    DialogsListAdapter.OnDialogLongClickListener<Dialogs> {

    override fun onDialogClick(dialog: Dialogs?) {
//        startActivity( Intent(this, MessengerActivity::class.java))
    }

    override fun onDialogLongClick(dialog: Dialogs?) {

    }
    companion object{
        var imageLoader =
            ImageLoader { imageView, url, payload ->
                Picasso.get().load(url).into(imageView)
            }
        val dialogsListAdapter = DialogsListAdapter<Dialogs>(imageLoader)

        var userCurrently= User()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialogmain)
        // lay userCurrent
        userCurrently= intent.getSerializableExtra("userCurrently") as User

        //lay hop thoai tu userCurrently va hien thi len man hinh
//        userCurrently.getListDialog().forEach {
//            getDialogs(it)
//        }
        val getData= DialogData()
        getData.getDataDialogOfUser(userCurrently,DialogActivity.dialogsListAdapter)
        dialogsList.setAdapter(dialogsListAdapter)
        dialogsListAdapter.setOnDialogClickListener {
            val intent= Intent(this, MessageActivity::class.java)
            intent.putExtra("idDialog", it.id)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_new_messager -> {
            startActivity(Intent(this, ListFriendActivity::class.java))
            true
        }
        R.id.menu_sign_out->{
            val auth= FirebaseAuth.getInstance()
            auth.signOut();
            //update
            startActivity(Intent(this, MainActivity::class.java))
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.naviga_menu,menu)
        return true
    }
}
