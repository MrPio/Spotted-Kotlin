package it.univpm.spottedkotlin.viewmodel

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.model.Post


class SpotPostViewModel( binding: SpotPostBinding) : ViewHolder(binding.root) {
	private val binding: SpotPostBinding
	init {
		this.binding=binding
	}

	fun bind(post: Post):Unit{
		binding.model=post
		binding.viewModel=this
		binding.executePendingBindings()
	}
	fun cardClicked(post: Post) {
			Toast.makeText(
			binding.root.context, "You clicked ${post.percentage}%",
			Toast.LENGTH_SHORT
		).show()
	}
}