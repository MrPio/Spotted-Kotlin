package it.univpm.spottedkotlin.enums

enum class Locations(val title: String? = null, val imageUrl: String? = null) {
	QT_140("Quota 140", RemoteImages.QT_140.url),
	QT_160("Quota 160", RemoteImages.QT_160.url)
}