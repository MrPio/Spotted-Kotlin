package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.enums.Tags
import it.univpm.spottedkotlin.extension.function.then
import it.univpm.spottedkotlin.model.*
import kotlin.math.min

object DataManager {
	enum class SaveMode { POST, PUT }

	const val pageSize = 100
	var posts: MutableList<Post> = mutableListOf()
	lateinit var tags: Set<Tags>
	lateinit var settingMenus: List<SettingMenu>
	val anonymous: User = User(
		name = "Anonimo",
		surname = "",
		gender = Gender.OTHER,
		avatar = RemoteImages.ANONYMOUS.url,
		cellNumber = null,
		instagramNickname = null,
		tags = mutableListOf(),
	)
	var cachedUsers: MutableSet<User> = mutableSetOf()


	// Fetch all the application's needed start data
	suspend fun fetchData(context: Context) {
		tags = Tags.values().toSet()
		cachedUsers = DatabaseManager.getList<User>("users", 9999)?.toMutableSet() ?: mutableSetOf()
		settingMenus = SeederManager.generateSettings(context)
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
		if (uid == null) return anonymous

		// Already cached?
		cachedUsers.find { it.uid == uid }?.let { return it }

		// Is current User?
		if (AccountManager.isUserInitialized && AccountManager.user.uid == uid) return AccountManager.user

		// Ask the database for the user and caching it
		DatabaseManager.get<User>("users/$uid")?.let { user ->
			user.uid = uid
			loadUserPosts(user)
			loadUserFollowingPosts(user)
			cachedUsers.add(user)
			return user
		}

		// If everything above failed
		return anonymous
	}

	// Load a single Post object from a given uid
	suspend fun loadPost(uid: String?): Post {

		// Is null?
		if (uid == null) return Post()

		// Already cached?
		posts.find { it.uid == uid }?.let { post ->
			loadPostInfo(post)
			return post
		}

		// Ask the database for the post and caching it
		DatabaseManager.get<Post>("posts/$uid")?.let { post ->
			post.uid = uid
			loadPostInfo(post)
			posts.add(post)
			return post
		}

		// If everything above failed
		return Post()
	}

	// Load the first 30 posts of a given user
	suspend fun loadUserPosts(user: User) {
//		user.posts.clear()
		for (postUID in user.postsUIDs.reversed().take(30))
			if (user.posts.find { it.uid == postUID } == null)
				DatabaseManager.get<Post>("posts/$postUID")?.let {
					it.uid = postUID
					user.posts.add(it)
				}
	}

	// Load the first 30 posts of a given user's following posts
	suspend fun loadUserFollowingPosts(user: User) {
//		user.followingPosts.clear()
		for (postUID in user.following.reversed().take(30))
			if (user.followingPosts.find { it.uid == postUID } == null)
				DatabaseManager.get<Post>("posts/$postUID")?.let {
					it.uid = postUID
					user.followingPosts.add(it)
				}
	}

	// Load post author, last followers and comments authors
	private suspend fun loadPostInfo(post: Post) {

		// Load the post author
		post.author = if (post.anonymous) anonymous else loadUser(post.authorUID)

		// Load the authors of comments
		post.comments.forEach { it.user = it.user ?: loadUser(it.authorUID) }

		// Carico gli ultimi 3 followers
		post.lastFollowers.clear()
		for (i in 1..min(3, post.followers.size))
			post.lastFollowers.add(loadUser(post.followers[post.followers.size - i]))
	}

	// Apply a given filter to the posts
	fun filterPosts(filter: Filter) {
		posts = filter.postFilter(posts).toMutableList()
	}

	// Save Model objects
	fun save(vararg models: Any, mode: SaveMode = SaveMode.PUT) {
		models.forEach { model ->
			val path = when (model) {
				is User -> "users/" + if (mode == SaveMode.PUT) model.uid else ""
				is Post -> "posts/" + if (mode == SaveMode.PUT) model.uid else ""
				else -> return
			}

			// Query the FirebaseRD
			if (mode == SaveMode.PUT) DatabaseManager.put(path, model)
			else DatabaseManager.post(path, model)?.let { uid ->
				// Any operations to be performed with the retrieved uid
				when (model) {
					is Post -> {
						// Add the newly created post to current user's posts list
						posts.add(model)
						AccountManager.user.postsUIDs.add(uid)
						AccountManager.user.posts.add(model)
						save(AccountManager.user)
					}
				}
			}
		}
	}
}