package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.*

object DataManager {
	enum class SaveMode { POST, PUT }

	const val pageSize = 60

	var posts: MutableList<Post> = mutableListOf()
	var tags: Set<Tag>? = null
	private val anonymous: User = User(
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
		tags= DatabaseManager.getList<Tag>("tags", pageSize = 999)?.toSet()
		cachedUsers= DatabaseManager.getList<User>("users",9999)?.toMutableSet() ?: mutableSetOf()
	}

	// Request a new page for paginated data
	suspend fun loadMore(pageSize:Int=this.pageSize) {
		posts.addAll(DatabaseManager.getList("posts", pageSize = pageSize) ?: listOf())
	}

	// Remove the last page pointer of paginated data
	fun reloadPaginatedData(){
		posts= mutableListOf()
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
		return user?: anonymous
	}

	// Apply a given filter to the posts
	fun filterPosts(filter: Filter) {
		posts = filter.postFilter(posts).toMutableList()
	}

    // Save model objects
    fun save(vararg model: Any, mode: SaveMode = SaveMode.PUT) {
        model.forEach {
            var path: String? = null
            var path_post: String =
                "users/" + AccountManager.user.uid + "/posts/"
            when (it) {
                is User -> path = "users/" + if (mode == SaveMode.PUT) it.uid else ""
                is Post -> {
                    path = "posts/" + if (mode == SaveMode.PUT) it.uid else ""
                    if (mode == SaveMode.POST) {
                        posts?.add(it)
                    }
                }
            }
            if (path != null)
                DatabaseManager.apply {
                    if (mode == SaveMode.PUT) put(path, it)
                    else {
                        var post_uid = post(path, it)
                        if (post_uid != null) {
							AccountManager.user.posts.add(post_uid)
                            save(AccountManager.user)
                        }
                    }
                }

        }
    }
}