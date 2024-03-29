package it.univpm.spottedkotlin.extension

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.databinding.UserCardBinding

/**
 * An [Observable] [ViewModel] for Data Binding.
 */
open class ObservableViewHolder(val view: View) : ViewHolder(view), Observable {

	private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

	override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) =
		callbacks.add(callback)

	override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) =
		callbacks.remove(callback)

	/**
	 * Notifies listeners that all properties of this instance have changed.
	 */
	@Suppress("unused")
	fun notifyChange() =
		callbacks.notifyCallbacks(this, 0, null)

	/**
	 * Notifies listeners that a specific property has changed. The getter for the property
	 * that changes should be marked with [Bindable] to generate a field in
	 * `BR` to be used as `fieldId`.
	 *
	 * @param fieldId The generated BR id for the Bindable field.
	 */
	fun notifyPropertyChanged(fieldId: Int) =
		callbacks.notifyCallbacks(this, fieldId, null)
}