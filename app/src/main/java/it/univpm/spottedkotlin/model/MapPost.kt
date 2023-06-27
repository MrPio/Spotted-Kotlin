package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toTimeStr
import it.univpm.spottedkotlin.interfaces.Validable
import org.osmdroid.util.GeoPoint
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class MapPost(val geoPoint: GeoPoint) : Post() {

}