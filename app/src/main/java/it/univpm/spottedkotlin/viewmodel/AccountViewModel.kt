package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

	val user:MutableLiveData<User> = MutableLiveData()

	fun initialize(uid: String?) {
		viewModelScope.launch {
			user.value = if (uid == null) AccountManager.user else DataManager.loadUser(uid)
			DataManager.loadUserPosts(user.value!!)
			DataManager.loadUserFollowingPosts(user.value!!)
		}
	}

	val posts: List<Post> get() = if (isCurrentUser) user.value!!.posts else user.value!!.posts.filter { !it.anonymous }
	val isCurrentUser get() = user.value!!.uid == AccountManager.user.uid
}

