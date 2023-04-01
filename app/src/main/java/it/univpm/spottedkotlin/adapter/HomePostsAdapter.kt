package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.SpotPostViewModel

class HomePostsAdapter(var posts: List<Post>) : Adapter<SpotPostViewModel>() {
	val LOADING_STEP=8
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotPostViewModel {
		val binding: SpotPostBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			R.layout.spot_post,
			parent,
			false
		)
		return SpotPostViewModel(binding)
	}

	override fun onBindViewHolder(holder: SpotPostViewModel, position: Int) {
		holder.bind(posts[position])
	}

	override fun getItemCount(): Int = posts.size
}