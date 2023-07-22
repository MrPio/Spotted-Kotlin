package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.enums.Tags
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
	var cachedPosts: MutableSet<Post> = mutableSetOf()
	var cachedUsers: MutableSet<User> = mutableSetOf()
	var cachedChats: MutableSet<Chat> = mutableSetOf()


	// Fetch all the application's needed start data
	suspend fun fetchData(context: Context) {
		tags = Tags.values().toSet()
		cachedUsers = DatabaseManager.getList<User>("users", 9999)?.toMutableSet() ?: mutableSetOf()
		settingMenus = SeederManager.generateSettings(context)
		cachedChats = DatabaseManager.getList<Chat>("chats", 9999)?.toMutableSet() ?: mutableSetOf()
	}

	// Request a new page for paginated data
	suspend fun loadMore(pageSize: Int = this.pageSize) {
		posts.addAll(DatabaseManager.getList("posts", pageSize = pageSize) ?: listOf())
		cachedPosts.addAll(posts.filter { post ->
			cachedPosts.find { cachedPost ->
				post.uid == cachedPost.uid
			} == null
		})
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
	suspend fun loadPost(uid: String?, withInfo: Boolean = true): Post {

		// Is null?
		if (uid == null) return Post()

		// Already cached?
		cachedPosts.find { it.uid == uid }?.let { post ->
			loadPostInfo(post)
			return post
		}

		// Ask the database for the post and caching it
		DatabaseManager.get<Post>("posts/$uid")?.let { post ->
			post.uid = uid
			if (withInfo) loadPostInfo(post)
			cachedPosts.add(post)
			return post
		}

		// If everything above failed
		return Post()
	}

	// Load the first 30 posts of a given user
	suspend fun loadUserPosts(user: User) {
		for (postUID in user.postsUIDs.reversed()
			.take(30)) if (user.posts.find { it.uid == postUID } == null) user.posts.add(
			loadPost(
				postUID, false
			)
		)
	}

	// Load the first 30 posts of a given user's following posts
	suspend fun loadUserFollowingPosts(user: User) {
		for (postUID in user.following.reversed()
			.take(30)) if (user.followingPosts.find { it.uid == postUID } == null) user.followingPosts.add(
			loadPost(postUID, false)
		)
	}

	// Load post author, last followers and comments authors
	private suspend fun loadPostInfo(post: Post) {

		// Load the post author
		post.author = if (post.anonymous) anonymous else loadUser(post.authorUID)

		// Load the authors of comments
		post.comments.forEach { it.user = it.user ?: loadUser(it.authorUID) }

		// Carico gli ultimi 3 followers
		post.lastFollowers.clear()
		for (i in 1..min(
			3, post.followers.size
		)) post.lastFollowers.add(loadUser(post.followers[post.followers.size - i]))
	}

	// Load a single Chat object from a given pair of user uids
	suspend fun loadChat(uid: String): Chat? {

		// Already cached?
		cachedChats.find { it.uid == uid }?.let { return it }

		// Ask the database for the user and caching it
		DatabaseManager.get<Chat>("chats/$uid")?.let { chat ->
			for (author in chat.authors) chat.users.add(loadUser(author))
			cachedChats.add(chat)
			return chat
		}

		// If everything above failed
		return null
	}

	suspend fun loadChat(firstUserUID: String, secondUserUID: String) =
		DataManager.loadChat(Chat(authors = mutableListOf(firstUserUID, secondUserUID)).uid)

	// Load the first 30 chats of a given user
	suspend fun loadUserChats(user: User) {
		for (userUID in user.chatsUserUID.reversed()
			.take(30)) if (user.chats.find { it.uid.contains(userUID) } == null) loadChat(user.uid?:"",userUID)?.let {
			user.chats.add(
				it
			)
		}
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
				is Chat -> "chats/" + if (mode == SaveMode.PUT) model.uid else ""
				else -> return
			}

			// Query the FirebaseRD
			if (mode == SaveMode.PUT) DatabaseManager.put(path, model)
			else DatabaseManager.post(path, model)?.let { uid ->
				// Any operations to be performed with the retrieved uid
				when (model) {
					is Post -> {
						// Add the newly created post to current user's posts list
						cachedPosts.add(model)
						AccountManager.user.postsUIDs.add(uid)
						AccountManager.user.posts.add(model)
						save(AccountManager.user)
					}
				}
			}
		}
	}
}