package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post


class CommentsViewModel(
	val post: Post,
	val nuovoCommentoCallback: () -> Unit,
	val emojiToggleCallback: () -> Unit,
	val loadEmojiCallback: (Int) -> Unit,
) :
	ObservableViewModel() {
	init {
		post.comments.sortByDescending { it.date }
	}

	@get:Bindable
	var emojiVisible = false
		set(value) {
			field = value;notifyPropertyChanged(BR.emojiVisible)
		}

	@get:Bindable
	var newComment: String = ""

	private var currentType = 0
	fun commenta() {
		if (newComment.isEmpty()) return
		val comment = Comment(
			authorUID = AccountManager.user.uid, text = newComment
		)
		post.comments.add(0, comment)
		DatabaseManager.put("posts/${post.uid}/comments/${post.comments.size - 1}", comment)
		newComment = ""
		notifyChange()
		nuovoCommentoCallback()
		if (emojiVisible)
			emojiToggleCallback()
	}

	fun toggleEmoji(type: Int) {
		if (currentType != type)
			loadEmojiCallback(type)
		if (!emojiVisible || emojiVisible && currentType == type)
			emojiToggleCallback()
		currentType = type
	}
}