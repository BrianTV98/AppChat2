package com.example.appchat.Model

import com.google.firebase.database.Exclude
import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import java.io.Serializable

data class Dialogs(private val id:String, private val dialogPhoto:String, private val dialogName:String,
                       private val users:ArrayList<User>, private var lastMessage:Message?, private val unreadCount:Int) :
        IDialog<Message> {
        constructor():this("","","",ArrayList<User>(),null, 0)

        override fun getDialogPhoto(): String {
            return  dialogPhoto
        }

        override fun getUnreadCount(): Int {
            return  unreadCount
        }

        @Exclude
        override fun setLastMessage(message: Message?) {
            if (message != null) {
                this.lastMessage=message
            }
        }

        override fun getId(): String {
            return  id
        }
        @Exclude
        override fun getUsers(): MutableList<out IUser> {
            return users
        }
        @Exclude
        override fun getLastMessage(): Message ?{
            return  lastMessage
        }

        override fun getDialogName(): String {
            return  dialogName
        }
}