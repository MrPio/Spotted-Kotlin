package it.univpm.spottedkotlin.enums

enum class Plexuses(
	val title: String? = null,
	val locations: List<Locations> = listOf(),
) {
	INGEGNERIA(
		"Univpm - Ingegneria", listOf(
			Locations.QT_140,
			Locations.QT_145,
			Locations.QT_150,
			Locations.QT_155,
			Locations.QT_160,
			Locations.AULE_SUD,
		)
	),
	MEDICINA("Univpm - Medicina", listOf(Locations.MEDICINA)),
	ECONOMIA("Univpm - Economia", listOf(Locations.ECONOMIA)),
	MENSA("Mensa", listOf(Locations.MENSA)),
	CAVOUR("Piazza Cavour", listOf(Locations.CAVOUR)),
	ROMA("Piazza Roma", listOf(Locations.ROMA)),
	IV_NOVEMBRE("Piazza IV Novembre", listOf(Locations.IV_NOVEMBRE)),
	UGO_BASSI("Piazza Ugo Bassi", listOf(Locations.UGO_BASSI)),
	STAZIONE_FS("Stazione FS", listOf(Locations.STAZIONE_FS)),
}