package it.univpm.spottedkotlin.extension

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.slider.Slider
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) =
	Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).into(view, object : Callback {
		override fun onSuccess() = Unit
		override fun onError(e: Exception?) {
			Picasso.get().load(url).into(view)
		}
	})

@BindingAdapter("text", "parameter")
fun loadString(view: TextView, res: Int, parameter: String) {
	view.text = view.context.getString(res, parameter)
}

@BindingAdapter("markdown")
fun loadMarkdown(view: TextView, markdown: String) {
	val str = SpannableStringBuilder("")
	for ((i, slice) in markdown.split("**").withIndex()) {
		str.append(slice)
		if (i % 2 == 1)
			str.setSpan(StyleSpan(Typeface.BOLD), str.length-slice.length, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
	}
	view.text = str
}

@BindingAdapter("sliderValueAttrChanged")
fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener) {
	slider.addOnChangeListener { _, value, _ ->
		attrChange.onChange()
	}
}

@InverseBindingAdapter(attribute = "sliderValue")
fun getSliderValue(slider: Slider): Int {
	return slider.value.toInt()
}

@BindingAdapter("sliderValue")
fun setSliderValue(slider: Slider, value: Int) {
	if (slider.value.toInt() != value) {
		slider.value = value.toFloat()
	}
}

@BindingAdapter("marginLeft")
fun setMarginLeft(view: View, leftMargin: Float) {
	val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
	layoutParams.leftMargin = leftMargin.toInt()
	view.layoutParams = layoutParams
}
@BindingAdapter("marginRight")
fun setMarginRight(view: View, leftMargin: Float) {
	val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
	layoutParams.rightMargin = leftMargin.toInt()
	view.layoutParams = layoutParams
}

@BindingAdapter("layout_gravity")
fun setLayoutGravity(view: View, gravity: Int) {
	val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
	layoutParams?.gravity = gravity
	view.layoutParams = layoutParams
}