package it.univpm.spottedkotlin.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.CommentsAdapter
import it.univpm.spottedkotlin.databinding.CommentsActivityBinding
import it.univpm.spottedkotlin.databinding.EmojiItemBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Chat
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.CommentsViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class CommentsActivity : AppCompatActivity() {
	lateinit var binding: CommentsActivityBinding
	lateinit var viewModel: CommentsViewModel
	private lateinit var commentsAdapter: CommentsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = CommentsActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.commentsRecycler.layoutManager =
			LinearLayoutManager(this).apply { reverseLayout = true }

		MainScope().launch {
			val post = intent.getStringExtra("postUID")
				?.run { DataManager.cachedPosts.find { it.uid == this } }
			val chat = intent.getStringExtra("chatUserUID")
				?.run {
					AccountManager.user.chats.find { it.uid.contains(this) }
						?: Chat(authors = mutableListOf(AccountManager.user.uid!!, this)).apply {
							for (author in authors) users.add(DataManager.loadUser(author))
						}
				}
			viewModel = CommentsViewModel(post, chat, ::loadComments, ::emojiToggle, ::loadEmoji)
			binding.viewModel = viewModel
		if (post != null)
			commentsAdapter = CommentsAdapter(post.comments.toNullable(), post.authorUID ?: "")
		if (chat != null)
			commentsAdapter = CommentsAdapter(chat.messages.toNullable(), viewModel.otherUser?.uid ?: "",true)
		binding.commentsRecycler.adapter = commentsAdapter
		}
		binding.commentsBack.setOnClickListener { finish() }
		binding.commentsInfo.setOnClickListener {
			if (viewModel.post != null)
				this.showAlertDialog(
					title = "Info post",
					message = "Post del ${viewModel.post!!.dateStr()}, con ${viewModel.post!!.comments.size} commenti"
				)
			if (viewModel.chat != null)
				this.showAlertDialog(
					title = "Info chat",
					message = "Chat del ${viewModel.chat!!.date.toDateStr()}, con ${viewModel.chat!!.messages.size} messaggi"
				)
		}
		if (binding.commentsEmojiGrid.childCount < 1)
			loadEmoji()
	}

	private fun loadComments() {
		val comments =
			if (viewModel.post != null)
				viewModel.post!!.comments
			else if (viewModel.chat != null)
				viewModel.chat!!.messages
			else
				return
		for (comment in comments)
			if (!commentsAdapter.comments.contains(comment))
				commentsAdapter.comments.add(comment)
		commentsAdapter.comments.remove(null)
		commentsAdapter.comments.add(null)
		commentsAdapter.notifyDataSetChanged()
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
		binding.commentsEmojiScroll.smoothScrollTo(0, 0)
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