package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toggle
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.BenchmarkManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.min

class ViewPostViewModel : ObservableViewModel() {
	var postUID: String? = null
	var post = Post()

	@get:Bindable
	val lastComment: String
		get() {
			if (post.comments.size > 0) {
				val comment = post.comments[0]
				return "**${comment.user?.name} ${comment.user?.surname}** ${comment.text}"
			}
			return ""
		}

	@get:Bindable
	val penultimateComment: String
		get() {
			if (post.comments.size > 1) {
				val comment = post.comments[1]
				return "**${comment.user?.name} ${comment.user?.surname}** ${comment.text}"
			}
			return ""
		}

	@get:Bindable
	val bottomText: String
		get() = if (post.comments.size > 0) "Visualizza tutti i ${post.comments.size} commenti"
		else "Inserisci tu il primo commento!"

	@get:Bindable
	val lastFollower: String
		get() {
			if (post.lastFollowers.isNotEmpty() && post.lastFollowers[0] is User) return "${post.lastFollowers[0]!!.name} ${post.lastFollowers[0]!!.surname}"
			return ""
		}

	val date get() = post.date.toDateStr()

	@get:Bindable
	val lastFollowersAvatar: List<String>
		get() = post.lastFollowers.map { it?.avatar ?: RemoteImages.ANONYMOUS.url }

	@get:Bindable
	val following: Boolean
		get() = AccountManager.user.following.contains(post.uid)

	@get:Bindable
	val relevance: Int
		get() = post.calculateRelevance(AccountManager.user.tags)

	@get:Bindable
	val isMine: Boolean
		get() = post.authorUID == AccountManager.user.uid

	@get:Bindable
	val imageUrl: String
		get() = post.location?.imageUrl ?: RemoteImages.ANCONA.url

	suspend fun initialize() {
		BenchmarkManager.lap()
		post = DataManager.loadPost(postUID)
		BenchmarkManager.lap("loadPost")
		post.comments.sortByDescending { it.date }
		notifyChange()
		BenchmarkManager.lap("notifyChange")
	}

	fun follow() {
		val isFollowing=!following
		post.followers.toggle(AccountManager.user.uid, isFollowing)
		AccountManager.user.following.toggle(post.uid, isFollowing)
		MainScope().launch { initialize() }
	}

	fun save() {
		DataManager.save(post, AccountManager.user)
	}

	fun spotted() {
		post.spotted = true
		notifyChange()
		DataManager.save(post)
	}
}