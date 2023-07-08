import it.univpm.spottedkotlin.extension.function.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateExtensionsTest {

	lateinit var dateNow:Date
	lateinit var dateYesterday:Date
	lateinit var dateLastMonth:Date

	@Before
	fun setup(){
		dateNow=Date()
		dateYesterday=Date().addDays(-1)
		dateLastMonth=Date().addDays(-31)
	}

	@Test
	fun `test toPostStr for different time differences`() {
		assertEquals(dateNow.toPostStr(), "alcuni sec fa")
		assertEquals(dateYesterday.toPostStr(),SimpleDateFormat("EEEE", Locale.getDefault()).format(dateYesterday))
		assertEquals(dateLastMonth.toPostStr(), dateLastMonth.toShortDateStr())
	}

	@Test
	fun `test toConceptualStr when time difference is less than 24 hours`() {
		val now = Date().time
		val date = Date(now - (1000 * 3600))
		val expected = date.toTimeStr()
		val result = date.toConceptualStr()
		assertEquals(expected, result)
	}


}
