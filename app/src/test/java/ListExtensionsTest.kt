
import it.univpm.spottedkotlin.extension.function.toInt
import it.univpm.spottedkotlin.extension.function.toUpperCamelCase
import it.univpm.spottedkotlin.extension.function.toggle
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class ListExtensionsTest {

	private lateinit var listTest: MutableList<String>


	@Before
	fun setup() {
		listTest = mutableListOf("Pippo","Pluto","Paperino","Pluto","")
	}

	@Test
	fun `toggle test`(){
		Assert.assertEquals(
			listTest.toggle("Pluto"), mutableListOf("Pippo","Paperino","Pluto","")
		)
		Assert.assertEquals(
			listTest.toggle("Mattia"), mutableListOf("Pippo","Paperino","Pluto","", "Mattia")
		)
	}
}