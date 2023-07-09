package it.univpm.spottedkotlin.androidTest

import android.content.ContextWrapper
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.*
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.MainActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class newPostEspressoTest {

	@Before
	fun initialize() {
		AccountManager.user = SeederManager.defaultUser
		Intents.init()
	}

	@Test
	fun newPostTest() {
		runBlocking {

			// Launch the activity
			val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
			val context = InstrumentationRegistry.getInstrumentation().targetContext
			DataManager.fetchData(context)
			DeviceManager.loadUiDensity(context)
			IOManager.initialize(context)


			onView(withId(R.id.add_post_icon)).perform(click())

			// Seleziona l'opzione non anonimo
			onView(withId(R.id.autore_spinner)).perform(scrollTo(), click())
			onData(CoreMatchers.anything()).atPosition(0).perform(click())

			// Seleziona un plesso
			onView(withId(R.id.plesso_spinner)).perform(scrollTo(), click())
			onData(CoreMatchers.anything()).atPosition(1).perform(click())

			// Seleziona una zona
			onView(withId(R.id.zona_spinner)).perform(scrollTo(), click())
			onData(CoreMatchers.anything()).atPosition(2).perform(click())

			// Seleziona un genere
			onView(withId(R.id.genere_spinner)).perform(scrollTo(), click())
			onData(CoreMatchers.anything()).atPosition(0).perform(click())

			// Seleziono 2 tags
			onView(withId(R.id.add_tag)).perform(scrollTo(), click())
			Thread.sleep(1000)
			onView(withText("Alto")).perform( click())
			onView(withText("Camicia")).perform( click())
			onView(withText("AGGIUNGI")).perform( click())

			// Scrivo descrizione non valida
			onView(withId(R.id.descizione_editText)).perform(scrollTo(), ViewActions.typeText(("desc")))

			// Pubblica button
			onView(withId(R.id.add_post_pubblica_button)).perform( click())

			// Assertion
			val errorText= listOf(
				"La descrizione deve essere presente e non più lunga di 1000 caratteri.",
			"Specifica almeno 3 tags"
			).joinToString(separator = "\n") { "• $it" }
			onView(withText(errorText)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()))

			activityScenario.close()
		}
	}
}
