package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.toConceptualStr
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Comment

class CommentItemViewModel(val comment: Comment) : ObservableViewModel() {
	@get:Bindable
	var full: Boolean = true
		set(value) {
			field = value; notifyPropertyChanged(BR.full)
		}
	val date: String get() = comment.date.toConceptualStr()

	val isMe: Boolean get() = comment.authorUID == AccountManager.user.uid
	var isAuthor: Boolean = false

	suspend fun initialize() {
		if (comment.user == null)
			comment.user = DataManager.loadUser(comment.authorUID)
		notifyChange()
	}
}