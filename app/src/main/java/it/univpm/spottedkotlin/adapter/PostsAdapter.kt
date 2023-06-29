package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.VSpaceBinding
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder

class PostsAdapter(var posts: MutableList<Post>, private var loaded: Int = 0) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	companion object {
		private const val VIEW_TYPE_POST = 0
		private const val VIEW_TYPE_LOADING = 1
	}

	fun updatePosts(posts: List<Post>) {
		this.posts.clear()
		this.posts.addAll(posts)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return if (viewType == VIEW_TYPE_POST) {
			val binding = SpotPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
			SpotPostViewHolder(binding)
		} else {
			val binding = VSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
			LoadingViewHolder(binding)
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is SpotPostViewHolder) {
			val post = posts[position] as Post
			holder.bind(post)
		} else if (holder is LoadingViewHolder) {
			// Bind loading view if needed
		}
	}

	override fun getItemCount(): Int {
		return posts.size
	}

	override fun getItemViewType(position: Int): Int {
		return if (posts[position] == null) {
			VIEW_TYPE_LOADING
		} else {
			VIEW_TYPE_POST
		}
	}

	inner class SpotPostViewHolder(private val binding: SpotPostBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(post: Post) {
			// Bind post data to views
		}
	}

	inner class LoadingViewHolder(private val binding: VSpaceBinding) : RecyclerView.ViewHolder(binding.root) {
		// Handle loading view if needed
	}
}
