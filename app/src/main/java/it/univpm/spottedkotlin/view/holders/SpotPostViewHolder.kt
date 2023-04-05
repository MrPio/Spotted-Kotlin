package it.univpm.spottedkotlin.view.holders

import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel

class SpotPostViewHolder(val binding: SpotPostBinding) : ViewHolder(binding.root) {
	fun bind(post: Post) {
		binding.model = post
		binding.viewModel = this

		for (tag in post.tags) {
			val tagBinding: TagItemBinding = binding.root.context.inflate(R.layout.tag_item)
			tagBinding.viewModel = TagItemViewModel()
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