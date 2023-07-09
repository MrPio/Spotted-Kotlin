
import it.univpm.spottedkotlin.extension.function.toInt
import it.univpm.spottedkotlin.extension.function.toPostStr
import it.univpm.spottedkotlin.extension.function.toUpperCamelCase
import it.univpm.spottedkotlin.extension.function.toggle
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.properties.Delegates

class ExtensionFunctionsTest {

	private lateinit var listTest: MutableList<String>
	private lateinit var stringToUpper: String
	private var boolTest by Delegates.notNull<Boolean>()


	@Before
	fun setup() {
		listTest = mutableListOf("Pippo","Pluto","Paperino","Pluto","")
		stringToUpper = "hello world !"
		boolTest = false
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

	@Test
	fun `string test`(){
		Assert.assertEquals(
			stringToUpper.toUpperCamelCase(), "HelloWorld!"
		)
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