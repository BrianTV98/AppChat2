package com.example.appchat.Message

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter

import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.Dialog.DialogActivity
import com.example.appchat.FetchData.MessageData
import com.example.appchat.Model.Message
import com.example.appchat.Model.User
import com.example.appchat.R
import com.google.firebase.storage.FirebaseStorage
import com.stfalcon.chatkit.commons.ImageLoader
import java.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*

class MessageActivity : AppCompatActivity(),MessagesListAdapter.OnLoadMoreListener,MessageInput.InputListener, MessageInput.AttachmentsListener,
    MessageInput.TypingListener {


    var userCurently: User =User()

    private val TOTAL_MESSAGES_COUNT = 100

    protected var imageLoader: ImageLoader?=null
    companion object{
        var senderId = "0"
        val imageLoader =
            ImageLoader { imageView, url, payload ->
                Picasso.get().load(url).into(imageView)
            }
        lateinit var messagesAdapter : MessagesListAdapter<Message>
        var idDialog: String=""
        private var messagesList: MessagesList? = null
    }

    private var menu: Menu? = null
    private var selectionCount: Int = 0
    private var lastLoadedDate: Date? = null

    var selectedPhotoUrl: Uri? = null // url của hình đc upload



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        // lay user dang nhap
        userCurently = DialogActivity.userCurrently
        // xac nhan nguoi dung
        senderId=userCurently.id
        messagesAdapter=MessagesListAdapter<Message>(senderId, Companion.imageLoader)
        // lay IdDialog hien tại
        idDialog= intent.getStringExtra("idDialog")
        // lay lich su tin nhan
        val getData= MessageData().getMessageHistory(idDialog, messagesAdapter)
        // cap nhap tin nhan ngay tai thoi diem hien tai
        val getLastMessage=MessageData().getMessageRealtime(idDialog, messagesAdapter)
        messagesAdapter.notifyDataSetChanged()
        messagesList!!.setAdapter(messagesAdapter)

        input.setInputListener(this)
        input.setAttachmentsListener(this)
    }
    //lay du lieu input gan len
    override fun onSubmit(input: CharSequence?): Boolean {
        val message = Message(getKey(), userCurently,input.toString())
        messagesAdapter!!.addToStart(message, true)

        // gan du lieu len firebase
        updateMessage(idDialog, message)
        return  true
    }

    private fun updateMessage(idDialog: Any, message: Message) {
        val ref= FirebaseDatabase.getInstance().getReference("/message/${idDialog}")
        val idMessage=  FirebaseDatabase.getInstance().getReference("/message/${idDialog}").push().key!!
        ref.child(idMessage).setValue(message)
    }

    fun getKey(): String{
        val key= FirebaseDatabase.getInstance().getReference("/message/").key!!
        return  key;
    }
    override fun onAddAttachments() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // thu muc image
        startActivityForResult(intent, 0)
    }

    private fun uploadImage() {
        val idImage= UUID.randomUUID().toString()
        Log.d("UUID",idImage)
        val ref= FirebaseStorage.getInstance()
            .getReference("/imageDialog/${idDialog}/${idImage}")

        Log.d("UUID","Load ko thanh cong")

        ref.putFile(selectedPhotoUrl!!)
            .addOnSuccessListener {
                Log.d("loadImage"," thanh cong")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Url", it.toString())

                    val message = Message(getKey(), userCurently,"",
                        Date(),it.toString()
                    )

                    updateMessage(idDialog,message)
                    messagesAdapter?.addToStart(message, true)
                }
            }
            .addOnFailureListener{
                Log.d("loadImage","That bai")
                return@addOnFailureListener
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Register", "Photo was selected")
            selectedPhotoUrl = data.data
            uploadImage()
        }
    }

    override fun onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status))
    }
    override fun onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status))
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    override fun onLoadMore(page: Int, totalItemsCount: Int) {
//        Log.i("TAG", "onLoadMore: $page $totalItemsCount")
//        if (totalItemsCount < TOTAL_MESSAGES_COUNT)
//        }
    }
}
