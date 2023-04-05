package it.univpm.spottedkotlin.enums

enum class Locations(val title: String? = null, val imageUrl: String? = null) {
	INGEGNERIA("Univpm - Ingegneria", RemoteImages.QT_140.url),
	MEDICINA("Univpm - Medicina", RemoteImages.QT_140.url),
	ECONOMIA("Univpm - Economia", RemoteImages.QT_140.url),
	MENSA("Mensa", RemoteImages.QT_140.url),
	CAVOUR("Piazza Cavour", RemoteImages.QT_140.url),
	ROMA("Piazza Roma", RemoteImages.QT_140.url),
	IV_NOVEMBRE("Piazza IV Novembre", RemoteImages.QT_140.url),
	UGO_BASSI("Piazza Ugo Bassi", RemoteImages.QT_140.url),
	STAZIONE_FS("Stazione FS", RemoteImages.QT_140.url),

	QT_140("Quota 140", RemoteImages.QT_140.url),
	QT_145("Quota 145", RemoteImages.QT_160.url),
	QT_150("Quota 150", RemoteImages.QT_160.url),
	QT_155("Quota 155", RemoteImages.QT_160.url),
	QT_160("Quota 160", RemoteImages.QT_160.url),
	AULE_SUD("Aule Sud", RemoteImages.QT_160.url),

}