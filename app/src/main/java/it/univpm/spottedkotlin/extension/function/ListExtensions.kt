package it.univpm.spottedkotlin.extension.function

fun <T> List<T>.randomList(lenght: Int): List<T> {
	val list = mutableListOf<T>()
	for (i in 1..lenght)
		list.add(this.random())
	return list
}