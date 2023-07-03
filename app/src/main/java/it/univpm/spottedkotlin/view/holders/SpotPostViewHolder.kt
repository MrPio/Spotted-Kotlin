package it.univpm.spottedkotlin.view.holders

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.toPostStr
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.ViewPostActivity
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SpotPostViewHolder(val binding: SpotPostBinding) : ViewHolder(binding.root) {

	val dataPost get() = binding.model?.date?.toPostStr()
	fun bind(post: Post) {
		binding.model = post
		binding.percentage = post.calculateRelevance(AccountManager.user.tags)
		binding.setView(this)

		binding.tagsLayout.removeAllViews()
		for (tag in post.tags) {
			val tagBinding: TagItemBinding = binding.root.context.inflate(R.layout.tag_item)
			tagBinding.viewModel = TagItemViewModel()
			tagBinding.model = tag
			binding.tagsLayout.addView(tagBinding.root)
		}
		binding.executePendingBindings()

		// Carico l'autore del post
		MainScope().launch {
			post.author =
				if (post.anonymous)
					DataManager.anonymous
				else
					DataManager.loadUser(post.authorUID)
			binding.avatar = post.author?.avatar
		}
	}


	fun cardClicked(post: Post) {
		val activity = binding.root.context as? Activity
		//val mainActivity = binding.root.context.getActivity<MainActivity>()
		val intent = Intent(activity, ViewPostActivity::class.java)
		intent.putExtra("postUID", post.uid)
		val option = ActivityOptions.makeSceneTransitionAnimation(
			activity,
			android.util.Pair(binding.backgroundImage, ViewPostActivity.TRANSITION_IMAGE),
		)
		activity?.startActivity(intent, option.toBundle())
	}
}