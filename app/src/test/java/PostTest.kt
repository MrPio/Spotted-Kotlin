import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Tags
import it.univpm.spottedkotlin.extension.function.randomList
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PostTest {

	private lateinit var nonValidPost1: Post;
	private lateinit var nonValidPost2: Post;
	private lateinit var validPost: Post;


	@Before
	fun setup() {
		nonValidPost1 = Post(
			description = "desc", tags = Tags.values().toList().randomList(5).toMutableList(),
		)
		nonValidPost2 = Post(
			description = "descrizione valida",
			tags = Tags.values().toList().randomList(2).toMutableList(),
			location = Locations.INGEGNERIA
		)
		validPost = Post(
			description = "descrizione valida",
			tags = Tags.values().toList().randomList(5).toMutableList(),
			location = Locations.INGEGNERIA
		)
	}

	@Test
	fun `validate with valid and invalid posts`() = runBlocking {
		Assert.assertTrue(nonValidPost1.validate()
			.find { it.contains("La descrizione deve essere presente") } != null && nonValidPost1.validate().size==2)
		Assert.assertTrue(nonValidPost2.validate()
			.find { it.contains("Specifica almeno 3 tags") } != null)
		Assert.assertEquals(
			validPost.validate().size, 0
		)
	}

}