package it.univpm.spottedkotlin.extension.function

fun <T> MutableList<T>.toggle(el: T?, add: Boolean? = null): MutableList<T> {
	if ((add == null && this.contains(el)) || add == false) el?.let { el ->
		this.removeAll { it == el }
	}
	else el?.let { this.add(it) }
	return this
}

fun <T> MutableList<T>.toNullable(): MutableList<T?> = this.map { it }.toMutableList()
