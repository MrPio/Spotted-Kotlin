package it.univpm.spottedkotlin.viewmodel

import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import java.util.zip.Inflater


class SpotPostViewModel(binding: SpotPostBinding) : ViewHolder(binding.root) {
	private val binding: SpotPostBinding

	init {
		this.binding = binding
	}

	fun bind(post: Post): Unit {
		binding.model = post
		binding.viewModel = this

		if (post.tags != null)
			for (tag in post.tags) {
				val tagBinding: TagItemBinding = DataBindingUtil.inflate(
					LayoutInflater.from(binding.root.context),
					R.layout.tag_item,
					binding.tagsLayout,
					false
				)
				tagBinding.model = tag
				binding.tagsLayout.addView(tagBinding.root)
			}
		binding.executePendingBindings()
	}

	fun cardClicked(post: Post) {
		Toast.makeText(
			binding.root.context, "You clicked ${post.percentage}%",
			Toast.LENGTH_SHORT
		).show()
	}
}