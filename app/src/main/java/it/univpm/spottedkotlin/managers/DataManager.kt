package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.*

object DataManager {
    enum class SaveMode { POST, PUT }

    const val pageSize = 50

    var posts: MutableList<Post> = mutableListOf()
    var tags: Set<Tag>? = null
    private val anonymous: User = User(
        name = "Anonimo",
        surname = "",
        gender = Gender.OTHER,
        cellNumber = null,
        instagramNickname = null,
        tags = mutableListOf(),
    )
    private var cachedUsers: MutableSet<User> = mutableSetOf()


    // Fetch all the application's needed start data
    suspend fun fetchData() {
        tags = DatabaseManager.getList<Tag>("tags", pageSize = 999)?.toSet()
    }

    suspend fun loadMore() {
        posts.addAll(DatabaseManager.getList("posts", pageSize = pageSize) ?: listOf())
        "loadMore(), posts=${posts.size}, paginateKeys=${DatabaseManager.paginateKeys}".log()
//		MapPost()
    }

    suspend fun reloadPaginatedData() {
        posts = mutableListOf()
        DatabaseManager.paginateKeys.clear() // To remove the last page pointer of paginated data
//		loadMore()
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
                            DatabaseManager.apply {
								post(path_post, post_uid)
                            }
							AccountManager.user.posts.add(post_uid)
                        }
                    }
                }

        }
    }
}