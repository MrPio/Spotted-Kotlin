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

class AccountViewModel(val user: User) : ViewModel() {

    var uid:String? = user.uid
    var avatar:String = user.avatar
    var name: String = user.name +" "+ user.surname
    var nameInsta: String? = " " + user.instagramNickname
    //val postsUid: MutableList<String> = user.postsUIDs
    val posts: MutableList<Post> = setPostsUid()
    var numPost:String= posts.size.toString()
    var numFollowing:String = user.following.size.toString()
    var numComment:String= user.comments.size.toString()
    var tags: MutableList<Tag> = user.tags

    fun setPostsUid(): MutableList<Post> {
        val posts = mutableListOf<Post>()
        if (user.uid == AccountManager.user.uid) return user.posts
        else{
            for (post in user.posts){
                if (!post.anonymous) posts.add(post)
            }
        }
        return posts
    }
}

