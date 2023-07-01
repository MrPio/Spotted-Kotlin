package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Filter
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class AccountViewModel(val user: User = AccountManager.user) : ViewModel() {
    //lateinit var user: User
    //lateinit var userPosts: MutableList<Post>

    var userPosts: MutableList<Post> = AccountManager.userPosts

    var name: String = user.name +" "+ user.surname
    var nameInsta: String? = " " + user.instagramNickname
    val postsUid: MutableList<String> = user.postsUIDs
    var numPost:String= postsUid.size.toString()
    var numFollowing:String = user.following.size.toString()
    var numComment:String= user.comments.size.toString()
    var tags: MutableList<Tag> = user.tags


//    suspend fun setUser (user: User){
//        if(user==AccountManager.user) {
//            this.user = AccountManager.user
//            this.userPosts = AccountManager.userPosts
//        }
//        else {
//            //this.user = DataManager.loadUser(uid)
//            //userPosts = DataManager.loadUserPost(uid)
//            this.user = user
//            this.userPosts = DataManager.loadUserPost(user.uid)
//        }
//    }
}

