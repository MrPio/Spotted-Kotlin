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

class AddPostTagsAdapter(
//TODO MIGRATE TO RECYCLERVIEW ADAPTER AND ADD GRID LAYOUT MANAGER
	private val addTagCallback: KFunction0<Unit>
) : BaseAdapter() {

	var tags: MutableList<Tag?> = mutableListOf()
		set(value) {
			field = value
			field.add(null)
			notifyDataSetChanged()
		}

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
			binding.onClickListener = View.OnClickListener { addTagCallback() }
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


