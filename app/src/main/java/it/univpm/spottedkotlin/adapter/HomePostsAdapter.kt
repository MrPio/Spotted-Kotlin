package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.SpotPostViewModel

class HomePostsAdapter(private val posts:List<Post>): Adapter<SpotPostViewModel>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotPostViewModel {
		// Create a new view, which defines the UI of the list item
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.spot_post, parent, false)
		val binding=SpotPostBinding.inflate(LayoutInflater.from(parent.context))
		return SpotPostViewModel(view,binding)
	}

	override fun onBindViewHolder(holder: SpotPostViewModel, position: Int) {
		holder.binding.model = posts[position]
	}

	override fun getItemCount(): Int =posts.size
}