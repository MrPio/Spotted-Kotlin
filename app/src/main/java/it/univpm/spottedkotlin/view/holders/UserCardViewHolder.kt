package it.univpm.spottedkotlin.view.holders

import android.app.Activity
import androidx.databinding.Bindable
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.databinding.UserCardBinding
import it.univpm.spottedkotlin.extension.ObservableViewHolder
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.extension.function.then
import it.univpm.spottedkotlin.extension.function.toggle
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.view.AccountActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.SettingsActivity

class UserCardViewHolder(val binding: UserCardBinding) : ObservableViewHolder(binding.root) {
	@get:Bindable
	lateinit var user: User
	fun bind(user: User) {
		this.user = user
		binding.viewHolder = this
		binding.userCardFollow.setOnClickListener { follow() }
	}

	fun onClick() =
		(binding.root.context as? Activity)?.goto<AccountActivity>(
			mapOf(
				"userUID" to ((AccountManager.user.uid != user.uid) then user.uid)
			)
		)

	private fun follow() {
		user.uid?.let {
			AccountManager.user.followingUsers.toggle(it)
			DataManager.save(AccountManager.user)
		}
		notifyPropertyChanged(BR.user)
	}
}