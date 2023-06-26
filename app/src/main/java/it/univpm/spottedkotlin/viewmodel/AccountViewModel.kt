package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class AccountViewModel : ViewModel() {

    val PICK_IMAGE_REQUEST = 1

    var name: String = AccountManager.user.name +" "+ AccountManager.user.surname
    var nameInsta:String=setNameInsta()
    var numPost:String=setNumPost()
    var numFollowing:String=setNumFollowing()
    var numComment:String=setNumComment()

    fun setNameInsta(): String{
        if(AccountManager.user.instagramNickname != null){
            return AccountManager.user.instagramNickname!!
        }
        else return ""
    }

    fun setNumPost():String{
        return AccountManager.user.posts.size.toString()
    }

    fun setNumFollowing(): String {
        return AccountManager.user.following.size.toString()
    }

    fun setNumComment(): String {
        return AccountManager.user.comments.size.toString()
    }

    // TODO: Implement the ViewModel
}