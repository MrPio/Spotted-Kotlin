package it.univpm.spottedkotlin.view.holders

import android.util.LayoutDirection
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.databinding.CommentItemBinding
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.viewmodel.CommentItemViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CommentItemViewHolder(val binding: CommentItemBinding) : ViewHolder(binding.root) {
	fun bind(comment: Comment, full: Boolean = true, isAuthor: Boolean = false) {
		val viewModel = CommentItemViewModel(comment).apply { this.isAuthor = isAuthor }
		binding.viewModel = viewModel
		viewModel.full = full
		MainScope().launch { viewModel.initialize() }
	}
}