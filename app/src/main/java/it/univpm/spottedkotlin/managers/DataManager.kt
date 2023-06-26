package it.univpm.spottedkotlin.managers

import android.content.Context
import com.google.android.gms.common.AccountPicker
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.extension.function.randomList
import it.univpm.spottedkotlin.model.*

object DataManager {
	enum class SaveMode { POST, PUT }

	var posts: MutableList<Post>? = null
	var tags: Set<Tag>? = null
	val anonymous: User = User(
		name = "Anonimo",
		surname = "",
		gender = Gender.OTHER,
		cellNumber = null,
		instagramNickname = null,
		tags = mutableListOf(),
	)
	var cachedUsers: MutableSet<User> = mutableSetOf()


	// Fetch all the application's needed data
	suspend fun fetchData(context: Context) {
		posts = DatabaseManager.getList<Post>("posts", limit = 999)?.toMutableList()
//		tags= DatabaseManager.getList<Tag>("tags", limit = 999)?.toSet()
		tags = DummyManager.generateTags(context)
		sort()
	}

	// Load a single User object from a given uid
	suspend fun loadUser(uid: String?): User? {

		// Is anonymous?
		if (uid == null)
			return anonymous

		// Already cached?
		val cachedUser = cachedUsers.find { it.uid == uid }
		if (cachedUser != null)
			return cachedUser

		// Is current User?
		if (AccountManager.user.uid == uid)
			return AccountManager.user

		// Asking the database for the user and caching it
		val user = DatabaseManager.get<User>("users/$uid")
		if (user != null) {
			user.uid = uid
			cachedUsers.add(user)
		}
		return user
	}

	// Sort all the app data
	fun sort() {
		posts?.sortByDescending { it.date }
	}

	// Apply a given filter to the posts
	fun filteredPosts(filter: Filter) =
		filter.postFilter(posts?.toList() ?: listOf())

	// Save model objects
	fun save(vararg model: Any,mode: SaveMode = SaveMode.PUT) {
		model.forEach {
			var path: String? = null
			when (it) {
				is User -> path = "users/" + if (mode == SaveMode.PUT) it.uid else ""
				is Post -> {
					path = "posts/" + if (mode == SaveMode.PUT) it.uid else ""
					if (mode == SaveMode.POST)
						posts?.add(it)
				}
			}
			if (path != null)
				DatabaseManager.apply {
					if (mode == SaveMode.PUT) put(path, it) else post(path, it)
				}
		}
	}
}