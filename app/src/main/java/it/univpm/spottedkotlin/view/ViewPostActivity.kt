package it.univpm.spottedkotlin.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.databinding.ViewPostActivityBinding
import it.univpm.spottedkotlin.extension.function.addViewLast
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.loadUrl
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import it.univpm.spottedkotlin.viewmodel.ViewPostViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ViewPostActivity : AppCompatActivity() {
	companion object {
		const val TRANSITION_IMAGE = "viewPostImageTransition"
	}

	lateinit var binding: ViewPostActivityBinding
	lateinit var viewModel: ViewPostViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ViewPostActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val postUID = intent.getStringExtra("postUID")
		val post = DataManager.posts.find { it.uid == postUID }
		viewModel = ViewPostViewModel(post ?: Post())
		initialize()
	}

	override fun onResume() {
		super.onResume()
		initialize()
	}

	private fun initialize() {
		binding.viewPostImage.transitionName = TRANSITION_IMAGE
		binding.viewModel = viewModel
		viewModel.post.location?.imageUrl?.let { binding.viewPostImage.loadUrl(it) }
		binding.exitOnClick = View.OnClickListener { finishAfterTransition() }
		binding.commentsOnClick = View.OnClickListener {
			val intent = Intent(this, CommentsActivity::class.java)
			intent.putExtra("postUID", viewModel.post.uid)
			this.startActivity(intent)
		}
		MainScope().launch {
			viewModel.initialize()
		}
		loadTags()
	}

	private fun loadTags() {
		val grid = binding.viewPostTagsGrid
		grid.removeAllViews()
		//Tags
		for (tag in viewModel.post.tags) {
			val tagBinding: TagItemBinding = this.inflate(R.layout.tag_item)
			tagBinding.model = tag
			tagBinding.viewModel = TagItemViewModel()
			grid.addViewLast(tagBinding.root)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		viewModel.save()
	}

}