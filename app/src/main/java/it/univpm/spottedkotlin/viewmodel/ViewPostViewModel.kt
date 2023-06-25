package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toggle
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.min

class ViewPostViewModel(val post: Post) : ObservableViewModel() {
	init {
		post.comments.sortByDescending { it.date }
	}

	@get:Bindable
	val lastComment: String
		get() {
			if (post.comments.size > 0) {
				val comment = post.comments.last()
				return "**${comment.user?.name} ${comment.user?.surname}** ${comment.text}"
			}
			return ""
		}

	@get:Bindable
	val penultimateComment: String
		get() {
			if (post.comments.size > 1) {
				val comment = post.comments[post.comments.size - 2]
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
			if (post.lastFollowers.isNotEmpty() && post.lastFollowers[0] is User)
				return "${post.lastFollowers[0]!!.name} ${post.lastFollowers[0]!!.surname}"
			return ""
		}

	val date get() = post.date.toDateStr()

	@get:Bindable
	val lastFollowersAvatar: List<String>
		get() = post.lastFollowers.map { it?.avatar ?: RemoteImages.ANONNYMOUS.url }

	@get:Bindable
	val following: Boolean get() =
		AccountManager.user.following.contains(post.uid)

	suspend fun initialize() {

		// Carico l'autore del post
		post.author = DataManager.loadUser(post.authorUID)

		// Carico gli autori dei commenti
		for (comment in post.comments)
			if (comment.user == null)
				comment.user = DataManager.loadUser(comment.authorUID)

		// Carico gli ultimi 3 followers
		post.lastFollowers.clear()
		for (i in 1..min(3, post.followers.size))
			post.lastFollowers.add(DataManager.loadUser(post.followers[post.followers.size - i]))

		notifyChange()
//		following.toString().log()
//		"${AccountManager.user.following.toString()}  ---  ${post.uid}".log()
	}

	fun follow() {
		val user = AccountManager.user
		post.followers.toggle(user.uid)
		user.following.toggle(post.uid)

		DataManager.save(post, user)

		MainScope().launch { initialize() }
	}
}