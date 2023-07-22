package it.univpm.spottedkotlin.viewmodel

import android.accounts.Account
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Chat
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.launch
import java.util.*


class CommentsViewModel(
	val post: Post?,
	val chat: Chat?,
	val loadCommentsCallback: () -> Unit,
	val emojiToggleCallback: () -> Unit,
	val loadEmojiCallback: (Int) -> Unit,
) : ObservableViewModel() {
	init {
		if (Settings.CHAT_OBSERVE.bool) {
			DatabaseManager.observeList<Comment>(if (post != null) "posts/${post.uid}/comments"
			else "chats/${chat!!.uid}/messages", observer = { comments ->
				for (comment in comments) {
					if (comment == null) continue
					val oldComments = post?.comments ?: chat!!.messages
					val oldComment = oldComments.find { it.timestamp == comment.timestamp }

					// Remove the already existing comment in order to replace with the new ones
					if (post != null) {
						if (oldComment != null) post.comments.remove(oldComment)
						post.comments.add(comment)
					}
					if (chat != null) {
						if (oldComment != null) chat.messages.remove(oldComment)
						chat.messages.add(comment)
					}
				}
				if (chat != null) {
					var changes = false
					for (comment in chat.messages.filter { it.authorUID != AccountManager.user.uid }) comment.apply {
						if (receivedTimestamp == null || readTimestamp == null) changes = true
						if (receivedTimestamp == null) receivedTimestamp =
							Calendar.getInstance().time.time
						if (readTimestamp == null) readTimestamp = Calendar.getInstance().time.time
					}
					if (changes)
						DataManager.save(chat)
				}

				// Update UI
				post?.comments?.sortByDescending { it.date }
				chat?.messages?.sortByDescending { it.date }
				loadCommentsCallback()
			})
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
		get() = if (Settings.CHAT_EMOJI.bool) View.VISIBLE else View.GONE

	val otherUser: User? get() = chat?.users?.find { it.uid != AccountManager.user.uid }

	fun commenta() {
		if (newComment.isEmpty()) return
		val comment = Comment(
			authorUID = AccountManager.user.uid, text = newComment
		)
		if (post != null) {
			post.comments.add(0, comment)
			DatabaseManager.put("posts/${post.uid}/comments/${post.comments.size - 1}", comment)
		}
		if (chat != null) {
			if (chat.messages.size == 0) {
				viewModelScope.launch {
					AccountManager.user.uid?.let { otherUser?.chatsUserUID?.add(it) }
					otherUser?.uid?.let { AccountManager.user.chatsUserUID.add(it) }
					otherUser?.let { DataManager.save(AccountManager.user, it, chat) }
				}
			}
			chat.messages.add(0, comment)
			DatabaseManager.put("chats/${chat.uid}/messages/${chat.messages.size - 1}", comment)
		}
		newComment = ""
		notifyChange()
		loadCommentsCallback()
		if (emojiVisible) emojiToggleCallback()
	}

	fun toggleEmoji(type: Int) {
		if (currentType != type) loadEmojiCallback(type)
		if (!emojiVisible || emojiVisible && currentType == type) emojiToggleCallback()
		currentType = type
	}
}