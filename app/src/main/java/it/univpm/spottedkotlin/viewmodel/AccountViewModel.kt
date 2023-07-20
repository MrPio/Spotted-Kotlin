package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.BenchmarkManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.launch

class AccountViewModel : ObservableViewModel() {

	val user: MutableLiveData<User> = MutableLiveData()

	@get:Bindable
	val accountName get() = "${user.value?.name ?: "Anonimo"} ${user.value?.surname ?: ""}"

	@get:Bindable
	val accountPostCount get() = "${user.value?.postsUIDs?.size ?: 0}"

	@get:Bindable
	val accountFollowingCount get() = "${user.value?.following?.size ?: 0}"

	@get:Bindable
	val accountCommentCount get() = "${user.value?.comments?.size ?: 0}"

	@get:Bindable
	val accountInstagramNickname get() = user.value?.instagramNickname ?: "non specificato"

	@get:Bindable
	var isCurrentUser = false

	fun initialize(uid: String?) {
		viewModelScope.launch {
			val user = if (uid == null) AccountManager.user else DataManager.loadUser(uid)
			DataManager.loadUserPosts(user)
			DataManager.loadUserFollowingPosts(user)
			this@AccountViewModel.user.value = user
			notifyChange()
		}
	}

	val posts: List<Post> get() = if (isCurrentUser) user.value!!.posts else user.value!!.posts.filter { !it.anonymous }
}

