package it.univpm.spottedkotlin.managers

import android.content.Context
import com.google.android.gms.common.AccountPicker
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.extension.function.randomList
import it.univpm.spottedkotlin.model.*

object DataManager {
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

	fun save(vararg model: Any) {
		model.forEach {
			var path: String? = null
			when (it) {
				is User -> path = "users/${it.uid}"
				is Post -> path = "posts/${it.uid}"
			}
			if (path != null)
				DatabaseManager.put(path, it)
		}
	}
}