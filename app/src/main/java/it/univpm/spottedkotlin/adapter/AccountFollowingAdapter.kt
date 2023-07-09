package it.univpm.spottedkotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.VSpaceBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder


class AccountFollowingAdapter(var posts: MutableList<Post>, private var loaded: Int = 0) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	companion object {
		private const val VIEW_TYPE_POST = 0
		private const val VIEW_TYPE_LOADING = 1
	}

	fun updatePosts(posts: List<Post>) {
		this.posts.clear()
		this.posts.addAll(posts)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
		if (viewType == VIEW_TYPE_POST)
			SpotPostViewHolder(parent.context.inflate(R.layout.spot_post))
		else
			object : RecyclerView.ViewHolder(parent.context.inflate<VSpaceBinding>(R.layout.v_space).root){}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is SpotPostViewHolder){
			holder.bind(posts[position],false)}
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
}
