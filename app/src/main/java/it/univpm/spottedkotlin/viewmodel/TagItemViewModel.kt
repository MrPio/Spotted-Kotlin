package it.univpm.spottedkotlin.viewmodel

import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag

class TagItemViewModel(val binding: TagItemBinding) : ViewHolder(binding.root) {
	fun bind(tag: Tag) {
		binding.model = tag
		binding.executePendingBindings()
	}
}