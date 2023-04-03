package it.univpm.spottedkotlin.viewmodel

import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel

class TagItemViewModel(
	private val selectable: Boolean = false,
	val onClickListener: (selected: Boolean) -> Unit = {},
) : ObservableViewModel() {

	@get:Bindable
	var selected = true

	init {
		if (selectable) {
			selected = false
		}
		notifyPropertyChanged(BR.selected)
	}

	fun onClick() {
		if (selectable) {
			selected = !selected
			notifyPropertyChanged(BR.selected)
			onClickListener(selected)
		}
	}
}