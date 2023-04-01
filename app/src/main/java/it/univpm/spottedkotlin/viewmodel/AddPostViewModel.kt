package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.model.Post

class AddPostViewModel : ObservableViewModel() {
	val plessi = Plexuses.values().map { it.title }
//	var zone = Plexuses.INGEGNERIA.locations?.map { it.title }

	val nuovoPost: Post = Post()
	var currentPlesso = Plexuses.values()[0]
	var currentZona = Locations.values()[0]

	init {
		nuovoPost.location = Plexuses.INGEGNERIA.locations?.get(0)
	}

	@get:Bindable
	val zone: List<String?>?
		get() = Plexuses.values()[plesso].locations?.map { it.title }

	@get:Bindable
	var plesso: Int
		get() = Plexuses.values().indexOf(currentPlesso)
		set(value) {
			currentPlesso = Plexuses.values()[value]
			notifyPropertyChanged(BR.plesso)
			notifyPropertyChanged(BR.zone)
		}


	@get:Bindable
	var zona: Int
		get() = Locations.values().indexOf(currentZona)
		set(value) {
			nuovoPost.location = Locations.values()[value]
			currentZona=Locations.values()[value]
			notifyPropertyChanged(BR.zona)
		}
}