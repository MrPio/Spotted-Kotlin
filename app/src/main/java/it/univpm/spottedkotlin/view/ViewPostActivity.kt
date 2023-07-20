package it.univpm.spottedkotlin.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.databinding.ViewPostActivityBinding
import it.univpm.spottedkotlin.extension.function.addViewLast
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.showAlertDialog
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import it.univpm.spottedkotlin.viewmodel.ViewPostViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ViewPostActivity : AppCompatActivity() {
	companion object {
		const val TRANSITION_IMAGE = "viewPostImageTransition"
	}

	lateinit var binding: ViewPostActivityBinding
	val viewModel: ViewPostViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val postUID = intent.getStringExtra("postUID")
		viewModel.postUID = postUID

		binding = ViewPostActivityBinding.inflate(layoutInflater)
		binding.viewPostImage.transitionName = TRANSITION_IMAGE
		binding.viewModel = viewModel
		binding.exitOnClick = View.OnClickListener { finishAfterTransition() }
		binding.accountOnClick = View.OnClickListener {
			if (viewModel.post.authorUID != null)
				this.goto<AccountActivity>(mapOf("userUID" to viewModel.post.authorUID))
		}
		binding.commentsOnClick = View.OnClickListener {
			gotoComments()
		}
		binding.viewPostSpotted.setOnClickListener { spotted() }

		setContentView(binding.root)
	}

	override fun onResume() {
		super.onResume()
		initialize()
	}

	private fun initialize() {
		MainScope().launch {
			binding.viewPostLoadingView.root.visibility=View.VISIBLE
			viewModel.initialize()
			loadTags()
			binding.viewPostLoadingView.root.visibility=View.GONE
		}

		if (intent.getBooleanExtra("comments", false)) {
			intent.removeExtra("comments")
			gotoComments()
		}
	}

	private fun gotoComments() = goto<CommentsActivity>(mapOf("postUID" to viewModel.post.uid))

	// Inflate the post tags inside the viewPostTagsGrid
	private fun loadTags() {
		val grid = binding.viewPostTagsGrid
		grid.removeAllViews()
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

	private fun spotted() {
		this.showAlertDialog(
			title = "Persona spottata",
			message = "Sei sicuro di aver spottato la persona del tuo post? In caso affermativo il post verrà archiviato. Attenzione, l'operazione è irreversibile",
			negativeCallback = {},
			positiveCallback = viewModel::spotted
		)
	}
}