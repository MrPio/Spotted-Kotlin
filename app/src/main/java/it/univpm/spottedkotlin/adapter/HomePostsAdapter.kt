package it.univpm.spottedkotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.VSpaceBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder

class HomePostsAdapter(var posts: MutableList<Post?>, var loaded: Int = 0) : Adapter<ViewHolder>() {
	companion object {
		private const val VIEW_TYPE_POST = 0
		private const val VIEW_TYPE_SPACE = 1
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		if (viewType == VIEW_TYPE_POST)
			SpotPostViewHolder(parent.context.inflate(R.layout.spot_post))
		else
			object : ViewHolder(parent.context.inflate<VSpaceBinding>(R.layout.v_space).root) {}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if (holder is SpotPostViewHolder)
			holder.bind(posts[position]!!)
	}

	override fun getItemCount(): Int = posts.size

	override fun getItemViewType(position: Int): Int =
		if (posts[position] == null) VIEW_TYPE_SPACE else VIEW_TYPE_POST
}