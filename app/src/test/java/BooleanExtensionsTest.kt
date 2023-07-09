
import it.univpm.spottedkotlin.extension.function.toInt
import it.univpm.spottedkotlin.extension.function.toUpperCamelCase
import it.univpm.spottedkotlin.extension.function.toggle
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class BooleanExtensionsTest {

	private var boolTest by Delegates.notNull<Boolean>()


	@Before
	fun setup() {
		boolTest = false
	}

	@Test
	fun `bool test`(){
		Assert.assertEquals(
			boolTest.toInt(), 0
		)
		boolTest=true

		Assert.assertEquals(
			boolTest.toInt(), 1
		)
	}

}