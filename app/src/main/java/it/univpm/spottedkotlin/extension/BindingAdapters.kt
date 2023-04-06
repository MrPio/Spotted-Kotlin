package it.univpm.spottedkotlin.extension

import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
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
