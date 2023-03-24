package it.univpm.spottedkotlin.extension.function

import android.content.Context
import android.content.ContextWrapper

fun Context.loadStr(res: Int) = this.resources.getString(res)
fun Context.loadColor(res: Int) = this.resources.getColor(res, theme)
inline fun <reified T> Context.getActivity(): T? {
	var context = this
	while (context is ContextWrapper) {
		if (context is T)
			return context
		context = context.baseContext
	}
	return null
}
