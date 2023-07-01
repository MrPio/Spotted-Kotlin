package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.*
import java.io.Serializable

object DataManager {
	enum class SaveMode { POST, PUT }

	const val pageSize = 100

	var posts: MutableList<Post> = mutableListOf()
	var tags: Set<Tag>? = null
	val anonymous: User = User(
		name = "Anonimo",
		surname = "",
		gender = Gender.OTHER,
		avatar = RemoteImages.ANONNYMOUS.url,
		cellNumber = null,
		instagramNickname = null,
		tags = mutableListOf(),
	)
	private var cachedUsers: MutableSet<User> = mutableSetOf()


	// Fetch all the application's needed start data
	suspend fun fetchData() {
		tags = DatabaseManager.getList<Tag>("tags", pageSize = 999)?.toSet()
		cachedUsers = DatabaseManager.getList<User>("users", 9999)?.toMutableSet() ?: mutableSetOf()
	}

	// Request a new page for paginated data
	suspend fun loadMore(pageSize: Int = this.pageSize) {
		posts.addAll(DatabaseManager.getList("posts", pageSize = pageSize) ?: listOf())
	}

	// Remove the last page pointer of paginated data
	fun reloadPaginatedData() {
		posts = mutableListOf()
		DatabaseManager.paginateKeys.clear()
	}

	// Load a single User object from a given uid
	suspend fun loadUser(uid: String?): User {

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

		// Ask the database for the user and caching it
		val user = DatabaseManager.get<User>("users/$uid")
		if (user != null) {
			user.uid = uid
			cachedUsers.add(user)
		}
		return user ?: anonymous
	}


	suspend fun loadUserPost(uid: String?): MutableList<Post> {
		val posts = mutableListOf<Post>()

		// Is anonymous?
		if (uid == null)
			return posts

		// Is current User?
		if (AccountManager.user.uid == uid)
			return AccountManager.userPosts

		// Ask the database for the userPost
		val user = DatabaseManager.get<User>("users/$uid")
		if (user != null) {
			for(post in user.posts){
				DatabaseManager.get<Post>("posts/"+post)?.let { posts.add(it) }
			}
		}
		return posts
	}

	// Apply a given filter to the posts
	fun filterPosts(filter: Filter) {
		posts = filter.postFilter(posts).toMutableList()
	}

	// Save Model objects
	fun save(vararg model: Any, mode: SaveMode = SaveMode.PUT) {
		model.forEach { model ->
			var path: String? = null
			path = when (model) {
				is User -> "users/" + if (mode == SaveMode.PUT) model.uid else ""
				is Post -> "posts/" + if (mode == SaveMode.PUT) model.uid else ""
				else -> return
			}

			// Query DB
			if (mode == SaveMode.PUT)
				DatabaseManager.put(path, model)
			else
				DatabaseManager.post(path, model)?.let { uid ->
					when (model) {
						is Post -> {
							posts.add(model)
							AccountManager.user.posts.add(uid)
							save(AccountManager.user)
						}
					}
				}
		}
	}
}