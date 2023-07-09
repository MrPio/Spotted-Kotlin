import it.univpm.spottedkotlin.extension.function.toInt
import it.univpm.spottedkotlin.extension.function.toUpperCamelCase
import it.univpm.spottedkotlin.extension.function.toggle
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class StringExtensionsTest {

	@Test
	fun `string test`() {
		Assert.assertEquals("hello world!".toUpperCamelCase(), "HelloWorld!")
		Assert.assertEquals("HEllo WORLD!".toUpperCamelCase(), "HelloWorld!")
		Assert.assertEquals("hello_world!".toUpperCamelCase(), "HelloWorld!")
		Assert.assertEquals("_hello_world_!".toUpperCamelCase(), "HelloWorld!")
	}

}