package it.univpm.spottedkotlin.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel

class TagsAdapter(
	private val tags: List<Tag?>,
	private val selectedTags: MutableSet<Tag>,
	private val tagClickCallback: (tag: Tag?, selected: Boolean) -> Unit,
) : BaseAdapter() {
	override fun getItem(position: Int): Any? = null
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val model = tags[position]
		val binding: TagItemBinding =
			if (convertView?.id == R.layout.tag_item)
				TagItemBinding.bind(convertView)
			else
				parent?.context!!.inflate(R.layout.tag_item, parent)
		binding.model = model
		binding.viewModel = TagItemViewModel(selectable = true) {
			tagClickCallback(binding.model, it)
		}.apply { selected = selectedTags.contains(model) }
		return binding.root
	}

	override fun getItemId(position: Int): Long = 0
	override fun getItemViewType(position: Int): Int = R.layout.tag_item

	override fun getCount(): Int = tags.size
}


