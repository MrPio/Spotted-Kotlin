package it.univpm.spottedkotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.view.holders.CommentItemViewHolder

class CommentsAdapter(var comments: MutableList<Comment>, private val postAuthorUID:String) : Adapter<CommentItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder =
		CommentItemViewHolder(parent.context.inflate(R.layout.comment_item))

	override fun onBindViewHolder(holder: CommentItemViewHolder, position: Int) {
		val comment = comments[position]
		holder.bind(
			comment,
			!(comments.size - 1 > position && comments[position + 1].authorUID == comment.authorUID),
			comment.authorUID==postAuthorUID
		)
	}

	override fun getItemCount(): Int = comments.size
}