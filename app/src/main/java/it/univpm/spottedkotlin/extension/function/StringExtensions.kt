package it.univpm.spottedkotlin.extension.function


fun String.toUpperCamelCase(): String {
	val words = this.split("_", " ")
	val upperCamelCase = StringBuilder()

	for (word in words) if (word.isNotEmpty()) {
		upperCamelCase.append(word[0].uppercase())
		upperCamelCase.append(word.substring(1).lowercase())
	}
	return upperCamelCase.toString()
}