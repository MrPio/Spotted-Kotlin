package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SpotPostBinding
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.toInt
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.SpotPostViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel

class AddPostTagsAdapter(private val tags: MutableList<Tag?>) : BaseAdapter() {
	override fun getItem(position: Int): Any? = null

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		if (getItemViewType(position) == R.layout.tag_item_add) {
			val binding: TagItemAddBinding =
				if (convertView?.id == R.layout.tag_item_add)
					TagItemAddBinding.bind(convertView)
				else
					DataBindingUtil.inflate(
						LayoutInflater.from(parent?.context),
						R.layout.tag_item_add,
						parent,
						false
					)
			binding.viewModel = TagItemAddViewModel()
			return binding.root
		} else {
			val binding: TagItemBinding =
				if (convertView?.id == R.layout.tag_item)
					TagItemBinding.bind(convertView)
				else
					DataBindingUtil.inflate(
						LayoutInflater.from(parent?.context),
						R.layout.tag_item,
						parent,
						false
					)
			binding.model = tags[position]
			binding.viewModel = TagItemViewModel()
			return binding.root
		}
	}

	override fun getItemId(position: Int): Long = 0
	override fun getItemViewType(position: Int): Int =
		if (position == tags.size - 1) R.layout.tag_item_add else R.layout.tag_item

	override fun getCount(): Int = tags.size
}


