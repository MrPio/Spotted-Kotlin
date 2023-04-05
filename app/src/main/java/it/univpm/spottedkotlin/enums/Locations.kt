package it.univpm.spottedkotlin.enums

enum class Locations(val title: String? = null, private val _imageUrl: String? = null) {
	//=== PLESSI ======================================================
	INGEGNERIA("Univpm - Ingegneria", RemoteImages.TORRE.url),
	AGRARIA("Agraria"),
	SCIENZE("Scienze", RemoteImages.SCIENZE_1.url),
	MENSA("Mensa"),
	MEDICINA("Univpm - Medicina"),
	ECONOMIA("Univpm - Economia"),
	//=================================================================

	//INGEGNERIA
	QT_140("Quota 140"),
	QT_150("Quota 150"),
	QT_155("Quota 155"),
	QT_160("Quota 160"),
	AULE_SUD("Aule Sud", RemoteImages.AULE_SUD_ATRIO.url),
	PORTINERIA("Portineria"),
	SEGRETERIA("Segreteria"),
	CLAB("Clab"),
	DIPARTIMENTO_MATEMATICA("Dip. Matematica/Copisteria"),
	BIBLIOTECA("Biblioteca"),

	//AGRARIA
	AGRARIA_ATRIO("Atrio"),
	AGRARIA_INGRESSO("Ingresso", RemoteImages.AGRARIA.url),
	AGRARIA_ZONA_STUDENTI("Zona studenti"),

	//SCIENZE
	SCIENZE_1("Scienze 1"),
	SCIENZE_2("Scienze 2"),
	SCIENZE_3("Scienze 3");

	val imageUrl get() = _imageUrl ?: RemoteImages.valueOf(this.name).url
}