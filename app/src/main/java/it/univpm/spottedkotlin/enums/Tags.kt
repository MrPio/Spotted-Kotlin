package it.univpm.spottedkotlin.enums

import it.univpm.spottedkotlin.extension.function.toUpperCamelCase

enum class Tags(val icon: String) {
	ALTO("\uDB83\uDEFC"), BASSO("\uDB83\uDEFC"),

	//Hair
	RICCI("\uDB84\uDCEF"), LISCI("\uDB84\uDCEF"),

	FELPA("\uDB82\uDE7B"), CAMICIA("\uDB82\uDE7B"), GIUBBOTTO("\uDB82\uDE7B"), CARDIGAN("\uDB82\uDE7B"),

	DA_VISTA("\uDB81\uDCE0"), DA_SOLE("\uDB81\uDCE0");

	val title get() = this.name.toUpperCamelCase();
}