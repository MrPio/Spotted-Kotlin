package it.univpm.spottedkotlin.extension

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel

/**
 * An [Observable] [AndroidViewModel] for Data Binding.
 */
open class ObservableAndroidViewModel(application: Application) : AndroidViewModel(application), Observable {

	private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

	override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
		callbacks.add(callback)
	}

	override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
		callbacks.remove(callback)
	}

	/**
	 * Notifies listeners that all properties of this instance have changed.
	 */
	@Suppress("unused")
	fun notifyChange() {
		callbacks.notifyCallbacks(this, 0, null)
	}

	/**
	 * Notifies listeners that a specific property has changed. The getter for the property
	 * that changes should be marked with [Bindable] to generate a field in
	 * `BR` to be used as `fieldId`.
	 *
	 * @param fieldId The generated BR id for the Bindable field.
	 */
	fun notifyPropertyChanged(fieldId: Int) {
		callbacks.notifyCallbacks(this, fieldId, null)
	}
}