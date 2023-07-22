package it.univpm.spottedkotlin.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.VSpaceChatBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.then
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.view.AccountActivity
import it.univpm.spottedkotlin.view.CommentsActivity
import it.univpm.spottedkotlin.view.holders.CommentItemViewHolder

class CommentsAdapter(var comments: MutableList<Comment?>, private val authorUID: String,val showCheck:Boolean=false) :
	Adapter<ViewHolder>() {

	companion object {
		private const val VIEW_TYPE_COMMENT = 0
		private const val VIEW_TYPE_SPACE = 1
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		if (viewType == VIEW_TYPE_COMMENT) CommentItemViewHolder(parent.context.inflate(R.layout.comment_item))
		else object :
			ViewHolder(parent.context.inflate<VSpaceChatBinding>(R.layout.v_space_chat).apply {
				val user = DataManager.cachedUsers.find { it.uid == authorUID }
				model = user
				onClick = View.OnClickListener {
					(it.context as? Activity)?.goto<AccountActivity>(mapOf("userUID" to user?.uid))
				}
			}.root) {}


	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val comment = comments[position]
		if (holder is CommentItemViewHolder && comment != null) holder.bind(
			comment,
			!(comments.size - 1 > position && comments[position + 1]?.authorUID == comment.authorUID),
			isAuthor = comment.authorUID == authorUID,
			showCheck = showCheck
		)
	}

	override fun getItemCount(): Int = comments.size

	override fun getItemViewType(position: Int): Int =
		if (comments[position] == null) VIEW_TYPE_SPACE else VIEW_TYPE_COMMENT

}