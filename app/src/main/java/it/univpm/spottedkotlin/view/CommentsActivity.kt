package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.CommentsAdapter
import it.univpm.spottedkotlin.databinding.CommentItemBinding
import it.univpm.spottedkotlin.databinding.CommentsActivityBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.CommentItemViewModel
import it.univpm.spottedkotlin.viewmodel.CommentsViewModel


class CommentsActivity : AppCompatActivity() {
	lateinit var binding: CommentsActivityBinding
	lateinit var viewModel: CommentsViewModel
	lateinit var commentsAdapter: CommentsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = CommentsActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val postUID = intent.getStringExtra("postUID")
		val post = DataManager.posts?.find { it.uid == postUID }
		viewModel = CommentsViewModel(post ?: Post(), ::loadComments)
		binding.viewModel = viewModel
		binding.commentsRecycler.layoutManager =
			LinearLayoutManager(this).apply { reverseLayout = true; stackFromEnd = true }
		commentsAdapter = CommentsAdapter(viewModel.post.comments,post?.authorUID?:"")
		binding.commentsRecycler.adapter = commentsAdapter
	}

	private fun loadComments() {
		val comments = viewModel.post.comments
		for (comment in comments.reversed())
			if (!commentsAdapter.comments.contains(comment))
				commentsAdapter.comments.add(0, comment)
		commentsAdapter.notifyItemRangeInserted(0, 1)
	}
}