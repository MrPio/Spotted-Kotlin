package it.univpm.spottedkotlin.enums

enum class Plexuses(val title: String? = null, val locations: List<Locations>? = null) {
	INGEGNERIA(
		"Univpm - Ingegneria", listOf(
			Locations.QT_140,
			Locations.QT_160,
		)
	),
	MEDICINA("Univpm - Medicina"),
	ECONOMIA("Univpm - Economia"),
	MENSA("Mensa"),
	CAVOUR("Piazza Cavour"),
	ROMA("Piazza Roma"),
	IV_NOVEMBRE("Piazza IV Novembre"),
	UGO_BASSI("Piazza Ugo Bassi"),
	STAZIONE_FS("Stazione FS"),
}