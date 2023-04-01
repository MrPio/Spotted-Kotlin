package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.SpotPostViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel

class AddPostTagsAdapter(var tags: MutableList<Tag>) : Adapter<ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding: SpotPostBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			R.layout.spot_post,
			parent,
			false
		)
		return SpotPostViewModel(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if (holder is TagItemViewModel)
			holder.bind(tags[position])
	}

	override fun getItemViewType(position: Int): Int = if(position == tags.size - 1)1 else 0

	override fun getItemCount(): Int = tags.size
}


