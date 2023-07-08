import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.SeederManager
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DataManagerTest {

	private lateinit var testUser: User

	@Before
	fun setup(){
		testUser= SeederManager.defaultUser
	}

	@Test
	fun `loadUser when uid is null`() = runBlocking {
		val result = DataManager.loadUser(null)
		assertEquals(DataManager.anonymous, result)
	}

	@Test
	fun `loadUser when user is already cached`() = runBlocking {
		val uid = "123"
		val cachedUser = testUser.apply { this.uid = uid }
		DataManager.cachedUsers.add(cachedUser)
		assertNotNull(DataManager.cachedUsers.find { it.uid == uid })

		val result = DataManager.loadUser(uid)
		assertEquals(cachedUser, result)
		assertEquals(DataManager.cachedUsers.filter { it.uid == uid }.size, 1)
	}

	@Test
	fun `loadUser when uid matches current user`() = runBlocking {
		val uid = "123"
		AccountManager.user = testUser.apply { this.uid=uid }

		val result = DataManager.loadUser(uid)
		assertEquals(testUser, result)

		assertEquals(uid, AccountManager.user.uid)
	}
}