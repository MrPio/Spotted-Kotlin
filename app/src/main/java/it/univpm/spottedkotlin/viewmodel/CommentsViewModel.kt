package it.univpm.spottedkotlin.viewmodel

import android.view.View
import androidx.databinding.Bindable
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post


class CommentsViewModel(
	val post: Post,
	val loadCommentsCallback: () -> Unit,
	val emojiToggleCallback: () -> Unit,
	val loadEmojiCallback: (Int) -> Unit,
) :
	ObservableViewModel() {
	init {
		post.comments.sortByDescending { it.date }
		if (Settings.CHAT_OBSERVE.bool) {
			DatabaseManager.observeList<Comment>(
				"posts/${post.uid}/comments",
				observer = { comments ->
					for (comment in comments) {
						if (comment == null)
							continue
						val oldComment = post.comments.find { it.date.time == comment.date.time }

						// Remove the already existing comment in order to replace with the new one
						if (oldComment != null)
							post.comments.remove(oldComment)
						post.comments.add(comment)
					}

					// Update UI
					post.comments.sortByDescending { it.date }
					loadCommentsCallback()
				}
			)
		}
	}

	@get:Bindable
	var emojiVisible = false
		set(value) {
			if (Settings.CHAT_EMOJI.bool) {
				field = value;
				notifyPropertyChanged(BR.emojiVisible)
			}
		}

	@get:Bindable
	var newComment: String = ""

	private var currentType = 0

	val emojiVisibility
		get() =
			if (Settings.CHAT_EMOJI.bool)
				View.VISIBLE
			else
				View.GONE

	fun commenta() {
		if (newComment.isEmpty()) return
		val comment = Comment(
			authorUID = AccountManager.user.uid, text = newComment
		)
		post.comments.add(0, comment)
		DatabaseManager.put("posts/${post.uid}/comments/${post.comments.size - 1}", comment)
		newComment = ""
		notifyChange()
		loadCommentsCallback()
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