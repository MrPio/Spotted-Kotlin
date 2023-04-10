package it.univpm.spottedkotlin.managers

import android.content.Context
import com.google.android.gms.common.AccountPicker
import it.univpm.spottedkotlin.model.Filter
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.model.User

object DataManager {
	var posts: MutableList<Post>? = null
	var tags: Set<Tag>? = null
	var cachedUsers: MutableSet<User> = mutableSetOf()

	suspend fun fetchData(context: Context) {
		posts = DatabaseManager.getList<Post>("posts", limit = 999)?.toMutableList()
//		tags= DatabaseManager.getList<Tag>("tags", limit = 999)?.toSet()
		tags = DummyManager.generateTags(context)
		sort()
	}

	suspend fun loadUser(uid: String): User? {
		//Already cached?
		val cachedUser = cachedUsers.find { it.uid == uid }
		if (cachedUser != null)
			return cachedUser
		//Is current User?
		if (AccountManager.user.uid == uid)
			return AccountManager.user
		//Asking the database for the user and caching it
		val user = DatabaseManager.get<User>("users/$uid")
		if (user != null) {
			user.uid = uid
			cachedUsers.add(user)
		}
		return user
	}

	fun sort() {
		posts?.sortByDescending { it.date }
	}

	fun filteredPosts(filter: Filter) =
		filter.postFilter(posts?.toList() ?: listOf())
}