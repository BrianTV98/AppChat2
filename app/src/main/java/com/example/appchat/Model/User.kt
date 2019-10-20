package com.example.appchat.Model
import com.google.firebase.database.IgnoreExtraProperties
import com.stfalcon.chatkit.commons.models.IUser
import java.io.Serializable

@IgnoreExtraProperties
data class User (private val id:String, private val name:String, private val avatar:String,
                 private  var listDialog:ArrayList<String>,private val  online: Boolean):
    IUser, Serializable {
    constructor(): this("","","",ArrayList<String>(),false)
    constructor(id: String,name: String, avatar: String,online: Boolean,list: ArrayList<String>): this(id,name,avatar,list,online)
    override fun getAvatar(): String {
        return avatar
    }

    override fun getName(): String {
        return  name
    }

    override fun getId(): String {
        return id
    }
    fun isOnline(): Boolean{
        return online
    }
    fun setListDialog( list: ArrayList<String> ){
        listDialog=list
    }
    fun getListDialog(): ArrayList<String>{
        return  listDialog
    }
}