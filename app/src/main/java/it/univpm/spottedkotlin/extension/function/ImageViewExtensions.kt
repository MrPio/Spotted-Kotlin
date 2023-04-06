package it.univpm.spottedkotlin.extension.function

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(url: String) {
	Picasso.get().load(url).noFade().noPlaceholder().networkPolicy(NetworkPolicy.OFFLINE)
		.into(this, object : Callback {
			override fun onSuccess() = Unit
			override fun onError(e: Exception?) {
				Picasso.get().load(url).noFade().noPlaceholder().into(this@loadUrl)
			}
		})
}