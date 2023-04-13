package it.univpm.spottedkotlin.managers

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.tasks.await
import kotlin.reflect.typeOf


object DatabaseManager {
	private const val tag = "FIREBASE"
	private val database = Firebase.database.reference
	fun getChild(
		path: String,
		database: DatabaseReference = this.database
	): DatabaseReference {
			val paths = path.split('/').filter { it.isNotEmpty() }
		val child = database.child(paths[0])
		return if (paths.count() <= 1) child else getChild(
			path = paths.drop(1).joinToString("/"),
			database = child
		)
	}

	fun put(path: String, res: Any) =
		getChild(path).setValue(res)

	fun post(path: String, res: Any): String? {
		val child = getChild(path)
		val key = child.push().key
		if (key == null) {
			Log.w(tag, "Couldn't get push key for posts")
			return null
		}
		put("$path/$key", res)
		return key
	}

	suspend inline fun <reified T> getList(
		path: String,
		limit: Int = 99,
	): List<T>? {
		val dataSnapshot = getChild(path).orderByKey().limitToFirst(limit).get().await()
		val map = dataSnapshot.getValue<HashMap<String, T>>() ?: return null
		when (T::class) {
			Post::class -> map.forEach { e -> (e.value as Post).uid = e.key }
		}
		return map.values.toList()
	}

	suspend inline fun <reified T> get(path: String): T? =
		getChild(path).get().await().getValue(T::class.java)

	fun <T> get(path: String, success: (it: T) -> Unit) {
		getChild(path).get().addOnSuccessListener {
			success(it.value as T)
		}
	}

	fun <T> observe(path: String, observer: (it: T) -> Unit) =
		getChild(path).addValueEventListener(object : ValueEventListener {
			override fun onDataChange(dataSnapshot: DataSnapshot) =
				observer(dataSnapshot.value as T)

			override fun onCancelled(error: DatabaseError) {
				Log.w(tag, "Failed to read value.", error.toException())
			}
		})

}