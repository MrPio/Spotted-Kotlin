package it.univpm.spottedkotlin.model

import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.managers.AccountManager
import java.time.Instant
import java.util.*

class Filter(
	private val minDate: Date = Date(Long.MIN_VALUE),
	private val maxDate: Date = Date(Long.MAX_VALUE),
	val plexus: Plexuses? = null,
	private val gender: Gender? = null,
	private val minPercentage: Int = 0,
	private val exceptUsers: List<String>? = null,
	private val onlyUsers: List<String>? = null
) {
	enum class OrderBy { RELEVANCE, DATE }

	var orderBy: OrderBy? = OrderBy.DATE
	fun postFilter(posts: List<Post>): List<Post> {
		val filtered = posts.filter {
			it.date.after(minDate) && it.date.before(maxDate) &&
					(plexus == null || it.location?.plexus == plexus) &&
					(gender == null || it.gender == gender) &&
					it.calculateRelevance(AccountManager.user.tags) >= minPercentage &&
					(exceptUsers == null || !exceptUsers.contains(it.authorUID)) &&
					(onlyUsers == null || onlyUsers.contains(it.authorUID))
		}
		return when (orderBy) {
			OrderBy.RELEVANCE -> filtered.sortedByDescending { it.calculateRelevance(AccountManager.user.tags) }
			OrderBy.DATE -> filtered.sortedByDescending { it.date }
			else -> filtered
		}
	}
}