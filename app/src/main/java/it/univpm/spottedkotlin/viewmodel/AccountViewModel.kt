package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Filter
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class AccountViewModel : ViewModel() {
    var user= AccountManager.user
    val filter = Filter(userId = user.uid)
    val postsRequestCompleted = CompletableDeferred<Unit>()

    var name: String = user.name +" "+ user.surname
    var nameInsta: String? = user.instagramNickname
    val postsUid: MutableList<String> = user.posts
    var numPost:String= postsUid.size.toString()
    var numFollowing:String = user.following.size.toString()
    var numComment:String= user.comments.size.toString()
    var tags: MutableList<Tag> = user.tags

    val userPost: MutableList<Post> = mutableListOf()


    suspend fun requestMorePosts() {
        DataManager.loadMore()
        DataManager.filterPosts(filter)
    }

    suspend fun getUserPost(){
        for (Uid in postsUid){
            DatabaseManager.get<Post>("posts/"+Uid)?.let { userPost.add(it) }
        }
        postsRequestCompleted.complete(Unit)
        //print(userPost+"\n\n\n\n\n") funziona
    }

}