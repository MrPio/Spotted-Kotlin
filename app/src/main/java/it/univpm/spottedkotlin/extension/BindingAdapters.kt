package it.univpm.spottedkotlin.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {

	val img=Picasso.get().load(url)
		img.into(view)
}
