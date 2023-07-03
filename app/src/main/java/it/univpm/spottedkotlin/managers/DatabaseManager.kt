package it.univpm.spottedkotlin.managers

import android.net.Uri
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.tasks.await

object DatabaseManager {
	const val tag = "FIREBASE"
	private val database = Firebase.database.reference
	private val storage = FirebaseStorage.getInstance().reference
	val paginateKeys: HashMap<String, String?> = hashMapOf()

	// Retrieve a child from a given path string
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

	// PUT -- store or replace an object at a given location
	fun put(path: String, res: Any) =
		getChild(path).setValue(res)

	// POST -- store new object at a given location
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

	// Async -- get a list of object and paginate it
	suspend inline fun <reified T> getList(
		path: String,
		pageSize: Int = 10,
		orderBy: String? = null
	): List<T>? {
		val lastKey = paginateKeys[path]

		if (paginateKeys.containsKey(path) && lastKey == null)
			return null

		// Query builder
		var query =
			if (orderBy == null)
				getChild(path).orderByKey()
			else
				getChild(path).orderByChild(orderBy)
		if (lastKey != null)
			query = query.endAt(lastKey)
		val dataSnapshot = query.limitToLast(pageSize).get().await()

		paginateKeys[path] = dataSnapshot.children.firstOrNull()?.key

		// Perdita dell'ordinamento, le mappe sono ordinate per chiavi (uid)
		val map = dataSnapshot.getValue<HashMap<String, T>>() ?: return null
		when (T::class) {
			Post::class -> map.forEach { e ->
				(e.value as Post).uid = e.key
			}
		}
		return map.values.toList()
	}

	// Async -- get a single object
	suspend inline fun <reified T> get(path: String): T? =
		getChild(path).get().await().getValue(T::class.java)

	// Sync -- get a single object
	fun <T> get(path: String, success: (it: T) -> Unit) {
		getChild(path).get().addOnSuccessListener {
			success(it.value as T)
		}
	}

	// Sync -- observe a list of objects
	inline fun <reified T> observeList(path: String, crossinline observer: (it: List<T?>) -> Unit) =
		getChild(path).addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(dataSnapshot: DataSnapshot) {
				observer(dataSnapshot.children.map { it.getValue(T::class.java) })
			}

			override fun onCancelled(error: DatabaseError) {
				Log.w(tag, "Failed to read value.", error.toException())
			}
		})

	inline fun <reified T> observeList2(path: String, crossinline observer: (it: T?) -> Unit) =
		getChild(path).addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
				observer(snapshot.getValue(T::class.java))
			}

			override fun onChildRemoved(snapshot: DataSnapshot) {
			}

			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
			}


			override fun onCancelled(error: DatabaseError) {
				Log.w(tag, "Failed to read value.", error.toException())
			}
		})

	// Sync -- upload file to a given URI in storage
	fun loadImg(localUrl: Uri) {
		val riversRef = storage.child("images/account_${AccountManager.user.uid}")
		val uploadTask = riversRef.putFile(localUrl)

		uploadTask.addOnSuccessListener { taskSnapshot ->
			// Ottieni l'URL dell'immagine caricata
			taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
				val uploadedImageUrl = uri.toString()

				// Imposta l'URL come valore di AccountManager.user.avatar
				AccountManager.user.avatar = uploadedImageUrl

				// Salva l'oggetto AccountManager.user nel database
				DataManager.save(AccountManager.user)
			}
		}
	}
}