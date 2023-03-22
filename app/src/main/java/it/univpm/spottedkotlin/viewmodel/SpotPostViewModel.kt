package it.univpm.spottedkotlin.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.model.Post

class SpotPostViewModel(itemView: View, binding: SpotPostBinding) : ViewHolder(itemView) {
	val binding: SpotPostBinding
	init {
		this.binding=binding
	}
}