package it.univpm.spottedkotlin.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.adapter.CommentsAdapter
import it.univpm.spottedkotlin.databinding.CommentsActivityBinding
import it.univpm.spottedkotlin.databinding.EmojiItemBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
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
		val post = DataManager.posts.find { it.uid == postUID }
		viewModel = CommentsViewModel(post ?: Post(), ::loadComments, ::emojiToggle, ::loadEmoji)
		binding.viewModel = viewModel
		binding.commentsRecycler.layoutManager =
			LinearLayoutManager(this).apply { reverseLayout = true; stackFromEnd = true }
		commentsAdapter = CommentsAdapter(viewModel.post.comments, post?.authorUID ?: "")
		binding.commentsRecycler.adapter = commentsAdapter

		if (binding.commentsEmojiGrid.childCount < 1)
			loadEmoji()
	}

	private fun loadComments() {
		val comments = viewModel.post.comments
		for (comment in comments.reversed())
			if (!commentsAdapter.comments.contains(comment))
				commentsAdapter.comments.add(0, comment)
		commentsAdapter.notifyItemRangeInserted(0, 1)
	}

	private fun loadEmoji(type: Int = 0) {
		val grid = binding.commentsEmojiGrid
		grid.removeAllViews()
		val emojisTxt = resources.loadTxt(R.raw.emoji) ?: return
		val emojisLists = emojisTxt.split("~")

		val list = emojisLists[type]
		val emojis = list.split("\r\n")
		for (emoji in emojis) {
			val emojiView = this.inflate<EmojiItemBinding>(R.layout.emoji_item)
			emojiView.text = emoji
			emojiView.emojiItemText.setOnClickListener {
				binding.commentsEdit.append((it as TextView).text)
				binding.commentsEdit.setSelection(binding.commentsEdit.length())
			}
			grid.addViewLast(emojiView.root)
		}
		binding.commentsEmojiScroll.smoothScrollTo(0,0)
	}

	private fun emojiToggle() {
		if (viewModel.emojiVisible) {
			AnimationManager.animate(240.fromDp(), 0, TimesInterpolator.ALMOST_LINEAR, update = {
				binding.commentsEmojiScroll.setHeight(it.toInt())
			}, endListener = { viewModel.emojiVisible = false })
		} else {
			viewModel.emojiVisible = true
			AnimationManager.animate(0, 240.fromDp(), TimesInterpolator.ALMOST_LINEAR, update = {
				binding.commentsEmojiScroll.setHeight(it.toInt())
			})
		}
	}
}