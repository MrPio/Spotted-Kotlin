package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.holders.SpotPostViewHolder

class HomePostsAdapter(var posts: List<Post>, var loaded: Int = 0) : Adapter<SpotPostViewHolder>() {
	val LOADING_STEP = 8
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotPostViewHolder =
		SpotPostViewHolder(parent.context.inflate(R.layout.spot_post))

	override fun onBindViewHolder(holder: SpotPostViewHolder, position: Int) =
		holder.bind(posts[position])

	override fun getItemCount(): Int = posts.size
}