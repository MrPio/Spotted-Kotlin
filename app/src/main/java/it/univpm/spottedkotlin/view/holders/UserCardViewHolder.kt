package it.univpm.spottedkotlin.view.holders

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.databinding.UserCardBinding
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.view.AccountActivity
import it.univpm.spottedkotlin.view.MainActivity

class UserCardViewHolder(val binding: UserCardBinding) : ViewHolder(binding.root) {

	fun bind(user: User) {
		binding.model=user
		binding.view=this
	}

	fun onClick(user: User) =
		if(AccountManager.user.uid==user.uid)
			(binding.root.context as MainActivity).viewModel.currentFragment.value = 3
		else
		(binding.root.context as? Activity)?.goto<AccountActivity>(mapOf("userUID" to user.uid))
}