package it.univpm.spottedkotlin.extension.function

import android.view.View
import android.widget.GridLayout

fun GridLayout.addViewLast(view: View) =
	this.addView(
		view, GridLayout.LayoutParams(
			GridLayout.spec(this.childCount / this.columnCount, GridLayout.START),
			GridLayout.spec(this.childCount % this.columnCount, GridLayout.START),
		)
	)
