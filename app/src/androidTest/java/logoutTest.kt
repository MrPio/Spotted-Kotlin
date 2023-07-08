package it.univpm.spottedkotlin.androidTest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.SeederManager
import it.univpm.spottedkotlin.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
class HelloWorldEspressoTest {

	@Before
	fun initialize(){
		AccountManager.user=SeederManager.defaultUser
	}

//	@get:Rule
//	val activityRule = ActivityScenarioRule(MainActivity::class.java)

	@Test
	fun gotoSettings() {
		// Launch the activity
		val activityScenario = ActivityScenario.launch(MainActivity::class.java)
		// Perform action: Click the button
		Espresso.onView(ViewMatchers.withId(R.id.home_menu)).perform(ViewActions.click())
		activityScenario.close()
	}
}
