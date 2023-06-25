package it.univpm.spottedkotlin.extension.function

fun <T> MutableList<T>.toggle(el: T?): MutableList<T> {
	if (this.contains(el))
		el?.let { this.remove(it) }
	else
		el?.let { this.add(it) }
	return this
}