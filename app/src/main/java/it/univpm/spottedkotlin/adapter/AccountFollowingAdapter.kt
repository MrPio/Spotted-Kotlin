package it.univpm.spottedkotlin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.VSpaceBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder


class AccountFollowingAdapter(var posts: MutableList<Post>, private var loaded: Int = 0) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
			SpotPostViewHolder(parent.context.inflate(R.layout.spot_post))

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is SpotPostViewHolder){
			holder.bind(posts[position],false)}
	}

	override fun getItemCount(): Int = posts.size
}
