package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User

class ViewPostViewModel(val post: Post) : ObservableViewModel() {
	@get:Bindable
	var author: String = ""
		set(value) {
			field = value
			notifyPropertyChanged(BR.author)
		}

	val date get() = post.date.toDateStr()

	suspend fun initialize() {
		author = DatabaseManager.get("users/${post.authorUID}/instagramNickname")
	}
}