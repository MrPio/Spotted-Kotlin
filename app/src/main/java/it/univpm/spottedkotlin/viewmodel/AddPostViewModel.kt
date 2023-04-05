package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.model.Post

class AddPostViewModel : ObservableViewModel() {
	val plessi = Plexuses.values().map { it.title }
	var genders = Gender.values().map { it.title }

	val nuovoPost: Post = Post()
	var currentPlesso = Plexuses.INGEGNERIA
	var currentZona = Locations.QT_140

	@get:Bindable
	val image: String?
		get() = currentPlesso.locations[zona].imageUrl


	init {
		nuovoPost.location = Plexuses.INGEGNERIA.locations.get(0)
	}

	@get:Bindable
	var plesso: Int
		get() = Plexuses.values().indexOf(currentPlesso)
		set(value) {
			currentPlesso = Plexuses.values()[value]
			currentZona = currentPlesso.locations[0]
			notifyPropertyChanged(BR.zone)
			notifyPropertyChanged(BR.image)
			notifyPropertyChanged(BR.plesso)
		}


	@get:Bindable
	val zone: List<String?>
		get() = Plexuses.values()[plesso].locations.map { it.title }

	@get:Bindable
	var zona: Int
		get() = currentPlesso.locations.indexOf(currentZona)
		set(value) {
			nuovoPost.location = Locations.values()[value]
			currentZona = currentPlesso.locations[value]
			notifyPropertyChanged(BR.zona)
			notifyPropertyChanged(BR.image)
		}

	@get:Bindable
	var currentGender = Gender.FEMALE

	@get:Bindable
	var gender: Int
		get() = currentGender.ordinal
		set(value) {
			currentGender = Gender.values()[value]
			nuovoPost.gender = currentGender
			notifyPropertyChanged(BR.gender)
			notifyPropertyChanged(BR.currentGender)
		}
}