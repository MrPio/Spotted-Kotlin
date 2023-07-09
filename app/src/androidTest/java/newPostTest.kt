package it.univpm.spottedkotlin.androidTest

import android.content.ContextWrapper
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
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

		Intents.init();
	}

	@Test
	fun newPostTest() {
		runBlocking {

			// Launch the activity
			val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
			DataManager.fetchData(InstrumentationRegistry.getInstrumentation().targetContext)
			DeviceManager.loadUiDensity(InstrumentationRegistry.getInstrumentation().targetContext)
			IOManager.initialize(InstrumentationRegistry.getInstrumentation().targetContext)


			onView(withId(R.id.add_post_icon)).perform(click())

			onView(withId(R.id.autore_spinner))
				.perform(ViewActions.scrollTo(), click());

			// Seleziona l'opzione desiderata tramite l'indice
			onData(CoreMatchers.anything())
				.atPosition(0)
				.perform(click());

			// Verifica che l'opzione selezionata sia corretta
			onView(withId(R.id.autore_spinner))
				.check(ViewAssertions.matches(withSpinnerText("Valerio Morelli")))
			//========================================================================

			//spinner plesso
			// Seleziona uno spinner specifico tramite il suo ID
			onView(withId(R.id.plesso_spinner))
				.perform(ViewActions.scrollTo(), click());

			// Seleziona l'opzione desiderata tramite l'indice
			onData(CoreMatchers.anything())
				.atPosition(1)
				.perform(ViewActions.click());

			// Verifica che l'opzione selezionata sia corretta
			onView(withId(R.id.plesso_spinner))
				.check(ViewAssertions.matches(withSpinnerText("Agraria")))

			//========================================================================

			//spinner zona
			// Seleziona uno spinner specifico tramite il suo ID
			onView(withId(R.id.zona_spinner))
				.perform(ViewActions.scrollTo(), click());

			// Seleziona l'opzione desiderata tramite l'indice
			onData(CoreMatchers.anything())
				.atPosition(2)
				.perform(ViewActions.click());

			// Verifica che l'opzione selezionata sia corretta
			onView(withId(R.id.zona_spinner))
				.check(ViewAssertions.matches(withSpinnerText("Atrio")))

			//========================================================================

			//spinner genere
			// Seleziona uno spinner specifico tramite il suo ID
			onView(withId(R.id.genere_spinner))
				.perform(ViewActions.scrollTo(), click());

			// Seleziona l'opzione desiderata tramite l'indice
			onData(CoreMatchers.anything())
				.atPosition(0)
				.perform(ViewActions.click());

			// Verifica che l'opzione selezionata sia corretta
			onView(withId(R.id.genere_spinner))
				.check(ViewAssertions.matches(withSpinnerText("Ragazzo")))


//			onData(Matchers.anything())
//				.inAdapterView(withId(R.id.add_post_tags_grid))
//				.atPosition(0)
//				.perform(click())

			//========================================================================

//			//scrivo descrizione
//			onView(withId(R.id.descizione_editText))
//				.perform(ViewActions.replaceText(("Test espresso")))

//			//========================================================================


//			//reset
//			onView(withId(R.id.add_post_reimposta_button))
//				.perform(click());
//
//			onView(withId(R.id.descizione_editText))
//				.perform(ViewActions.typeText(""));

			activityScenario.close()
		}
	}
}
