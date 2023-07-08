import android.content.Context
import android.content.SharedPreferences
import io.mockk.*
import io.mockk.impl.annotations.MockK
import it.univpm.spottedkotlin.managers.IOManager
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class IOManagerTest {

	@MockK
	private lateinit var context: Context

	@MockK
	private lateinit var sharedPreferences: SharedPreferences

	private lateinit var ioManager: IOManager

	@Before
	fun setup() {
		MockKAnnotations.init(this)
		every { context.getSharedPreferences(any(), any()) } returns sharedPreferences
		ioManager = IOManager
		IOManager.initialize(context)
	}

	@After
	fun tearDown() {
		unmockkAll()
	}

	@Test
	fun `writeKey with String value`() {
		val key = "uid"
		val value = "accountUID123"
		every { sharedPreferences.edit().putString(key, value).apply() } just Runs

		IOManager.writeKey(key, value)

		verify(exactly = 1) { sharedPreferences.edit().putString(key, value) }
	}

	@Test
	fun `readKey with String value`() {
		val key = "uid"
		val expectedValue = "accountUID123"
		every { sharedPreferences.all } returns mapOf(key to expectedValue)

		val result = IOManager.readKey(key)

		assertEquals(expectedValue, result)
	}
}
