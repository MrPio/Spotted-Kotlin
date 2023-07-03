package it.univpm.spottedkotlin.model

import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.managers.AccountManager
import java.util.*

class Filter(
	var minDate: Date = Date(Long.MIN_VALUE),
	var maxDate: Date = Date(Long.MAX_VALUE),
	var plexus: Plexuses? = null,
	var gender: Gender? = null,
	var minPercentage: Int = 0,
	var exceptUsers: List<String>? = null,
	var onlyUsers: List<String>? = null,
	var exceptSpotted: Boolean = false
) {
	enum class OrderBy { RELEVANCE, DATE }

	var orderBy: OrderBy? = OrderBy.DATE
	fun postFilter(posts: List<Post>): List<Post> {
		val filtered = posts.filter {
			it.date.after(minDate) && it.date.before(maxDate) &&
					(plexus == null || it.location?.plexus == plexus) &&
					(gender == null || it.gender == gender) &&
					it.calculateRelevance(AccountManager.user.tags) >= minPercentage &&
					(exceptUsers == null || !exceptUsers!!.contains(it.authorUID)) &&
					(onlyUsers == null || onlyUsers!!.contains(it.authorUID)) &&
					(!exceptSpotted || !it.spotted)
		}
		return when (orderBy) {
			OrderBy.RELEVANCE -> filtered.sortedByDescending { it.calculateRelevance(AccountManager.user.tags) }
			OrderBy.DATE -> filtered.sortedByDescending { it.timestamp }
			else -> filtered
		}
	}
}