package it.univpm.spottedkotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.view.holders.UserCardViewHolder

class SearchUserAdapter(var users: MutableList<User>) : Adapter<UserCardViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder =
		UserCardViewHolder(parent.context.inflate(R.layout.user_card))

	override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) =
		holder.bind(users[position])

	override fun getItemCount(): Int = users.size
}