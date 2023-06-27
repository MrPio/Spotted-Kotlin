package it.univpm.spottedkotlin.managers

import android.net.Uri
import android.util.Log
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
	private const val tag = "FIREBASE"
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

	// Sync -- observe a single object
	fun <T> observe(path: String, observer: (it: T) -> Unit) =
		getChild(path).addValueEventListener(object : ValueEventListener {
			override fun onDataChange(dataSnapshot: DataSnapshot) =
				observer(dataSnapshot.value as T)

			override fun onCancelled(error: DatabaseError) {
				Log.w(tag, "Failed to read value.", error.toException())
			}
		})

	// Sync -- upload file to a given URI in storage
	fun loadImg(localUrl: Uri) {
		val riversRef = storage.child("images/account_${AccountManager.user.uid}")
		val uploadTask = riversRef.putFile(localUrl)

		//TODO mettere le eccezioni
// Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener { taskSnapshot ->
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }
	}
}