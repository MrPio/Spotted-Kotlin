package it.univpm.spottedkotlin.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) =
	Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).into(view)

