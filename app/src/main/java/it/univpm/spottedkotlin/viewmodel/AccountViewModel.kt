package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class AccountViewModel : ViewModel() {
    var user= AccountManager.user

    var name: String = user.name +" "+ user.surname
    var nameInsta: String? = user.instagramNickname
    var posts: MutableList<String> = user.posts
    var numPost:String= posts.size.toString()
    var numFollowing:String = user.following.size.toString()
    var numComment:String= user.comments.size.toString()
    var tags: MutableList<Tag> = user.tags


}