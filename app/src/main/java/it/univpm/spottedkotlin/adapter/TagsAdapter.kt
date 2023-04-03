package it.univpm.spottedkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlin.reflect.KFunction0

class TagsAdapter(
	private val tags: MutableList<Tag?>
) : BaseAdapter() {
	override fun getItem(position: Int): Any? = null
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
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

	override fun getItemId(position: Int): Long = 0
	override fun getItemViewType(position: Int): Int = R.layout.tag_item

	override fun getCount(): Int = tags.size
}


