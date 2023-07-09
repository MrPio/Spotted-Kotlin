package it.univpm.spottedkotlin.androidTest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.SeederManager
import it.univpm.spottedkotlin.view.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HelloWorldEspressoTest {

	@Before
	fun initialize() {
		AccountManager.user = SeederManager.defaultUser
		Intents.init();
	}

	@Test
	fun logoutAndLoginTest() {
		runBlocking {

			// Launch the activity
			val activityScenario = ActivityScenario.launch(MainActivity::class.java)
			DataManager.fetchData(InstrumentationRegistry.getInstrumentation().targetContext)

			// LOGOUT
			Espresso.onView(withId(R.id.home_menu)).perform(ViewActions.click())
			Espresso.onView(withText("Logout")).perform(ViewActions.click())
			Espresso.onView(withText("Si")).perform(ViewActions.click())

			// LOGIN
			Espresso.onView(withText("Iniziamo")).perform(ViewActions.click())
			Espresso.onView(withId(R.id.TextEmail))
				.perform(ViewActions.typeText("valeriomorelli50@gmail.com"))
			Espresso.onView(withId(R.id.TextPassword)).perform(ViewActions.typeText("password"))

			// GOTO MAIN ACTIVITY
			Espresso.onView(withId(R.id.login_button)).perform(ViewActions.click())
			Intents.intended(hasComponent(hasClassName(MainActivity::class.java.name)))
			Thread.sleep(2000)
			Intents.release()
			// CHECK
			Espresso.onView(withText("Ciao, Valerio!")).check(ViewAssertions.matches(isDisplayed()))
			activityScenario.close()
		}
	}
}
