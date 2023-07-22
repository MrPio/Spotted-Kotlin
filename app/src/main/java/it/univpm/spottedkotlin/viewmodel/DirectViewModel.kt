package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Chat
import kotlinx.coroutines.launch

class DirectViewModel : ObservableViewModel() {
	private val chatsUID get() = AccountManager.user.chatsUserUID.toList()

	@get:Bindable
	val chats = MutableLiveData<List<Chat>>()

	fun initialize() {
		viewModelScope.launch {
			val chats = mutableListOf<Chat>()

			// Load the chat objects
			for (chatUID in chatsUID) AccountManager.user.uid?.let {
				DataManager.loadChat(
					it, chatUID
				)?.let { chats.add(it) }
			}

			// Put all the chat with unread messages first
			chats.sortWith(
				compareByDescending<Chat> { it.timestamp}
					.thenBy { chat -> chat.messages.find { msg -> msg.receivedTimestamp == null } != null  }
			)
			this@DirectViewModel.chats.value = chats
			notifyPropertyChanged(BR.chats)
		}
	}
}

