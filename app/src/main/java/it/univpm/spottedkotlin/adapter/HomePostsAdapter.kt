package it.univpm.spottedkotlin.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.VSpaceBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder

class HomePostsAdapter(var posts: MutableList<Post?>, var loaded: Int = 0) : Adapter<ViewHolder>() {
	val LOADING_STEP = 8
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		if (viewType == 0)
			SpotPostViewHolder(parent.context.inflate(R.layout.spot_post))
//			object : ViewHolder(parent.context.inflate<VSpaceBinding>(R.layout.v_space).root){}
		else
			object : ViewHolder(parent.context.inflate<VSpaceBinding>(R.layout.v_space).root){}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if (holder is SpotPostViewHolder)
			holder.bind(posts[position]!!)
		position.toString().log()
	}

	override fun getItemCount(): Int = posts.size

	override fun getItemViewType(position: Int): Int =
		if (posts[position] == null) 1 else 0
}