package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.model.Post

class AddPostViewModel : ObservableViewModel() {
	val plessi = Locations.values().map { it.title }
	val zone = "ciao abcd".split(' ')
	val nuovoPost: Post = Post()

	@get:Bindable
	var plesso: Int
		get() = Locations.values().indexOf(nuovoPost.location)
		set(value) {
			nuovoPost.location = Locations.values()[value]
			println(nuovoPost.location)
			notifyPropertyChanged(BR.plesso)
		}
}