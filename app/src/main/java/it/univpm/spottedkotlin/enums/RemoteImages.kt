package it.univpm.spottedkotlin.enums

enum class RemoteImages(private val _url: String = "") {
	TOP_SPRITE_001("top_sprite_001.png"),
	QT_140("uni_140.jpg"), QT_150, QT_155, QT_160("uni_160.jpg"),
	SCIENZE_1, SCIENZE_2, SCIENZE_3,
	PORTINERIA, SEGRETERIA,
	CLAB, DIPARTIMENTO_MATEMATICA, BIBLIOTECA,
	MENSA,
	MEDICINA,
	AULE_SUD, AULE_SUD_ATRIO,
	AGRARIA_ATRIO, AGRARIA_ZONA_STUDENTI, AGRARIA,
	TORRE,
	ECONOMIA;

	val url
		get() = "https://firebasestorage.googleapis.com/v0/b/spotted-f3589.appspot.com/o/src%2F" +
				"${_url.ifEmpty { this.name.lowercase()+".jpg" }}?alt=media"
}