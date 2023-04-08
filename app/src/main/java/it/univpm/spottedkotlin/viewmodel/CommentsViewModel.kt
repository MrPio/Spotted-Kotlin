package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post


class CommentsViewModel(val post: Post, val nuovoCommentoCallback:()->Unit) : ObservableViewModel() {
	init {
		post.comments.sortByDescending { it.date }
	}

	var newComment: String = ""

	fun commenta() {
		val comment=Comment(
			authorUID = AccountManager.user.uid,
			text = newComment
		)
		post.comments.add(0,comment)
		newComment=""
		notifyChange()
		nuovoCommentoCallback()
	}
}