package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.addDays
import it.univpm.spottedkotlin.extension.function.randomList
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.model.User
import java.util.*
import kotlin.random.Random

object DummyManager {
	object names {

		val names: List<String>
			get() = ("Abaco Abbondanzio " +
					"Abbondio Abdone " +
					"Abelardo Abele " +
					"Abenzio Abibo " +
					"Abramio Abramo " +
					"Acacio Acario " +
					"Accursio Achille " +
					"Acilio Aciscolo " +
					"Acrisio Adalardo " +
					"Adalberto Adalfredo " +
					"Adalgiso Adalrico " +
					"Adamo Addo " +
					"Adelardo Adelberto " +
					"Adelchi Adelfo " +
					"Adelgardo Adelmo " +
					"Adeodato Adolfo " +
					"Adone Adriano " +
					"Adrione Afro " +
					"Agabio Agamennone " +
					"Agapito Agazio " +
					"Agenore Agesilao " +
					"Agostino Agrippa " +
					"Aiace Aidano " +
					"Aimone Aladino " +
					"Alamanno Alano " +
					"Alarico Albano " +
					"Alberico Alberto " +
					"Albino Alboino " +
					"Albrico Alceo " +
					"Alceste Alcibiade " +
					"Alcide Alcino " +
					"Aldo Aldobrando " +
					"Aleandro Aleardo " +
					"Aleramo Alessandro " +
					"Alessio Alfio " +
					"Alfonso Alfredo " +
					"Algiso Alighiero " +
					"Almerigo Almiro " +
					"Aloisio Alvaro " +
					"Alviero Alvise " +
					"Amabile Amadeo " +
					"Amando Amanzio " +
					"Amaranto Amato " +
					"Amatore Amauri " +
					"Ambrogio Ambrosiano " +
					"Amedeo Amelio " +
					"Amerigo Amico " +
					"Amilcare Amintore " +
					"Amleto Amone " +
					"Amore Amos " +
					"Ampelio Anacleto " +
					"Andrea Angelo " +
					"Aniceto Aniello " +
					"Annibale Ansaldo " +
					"Anselmo Ansovino " +
					"Antelmo Antero " +
					"Antimo Antino " +
					"Antioco Antonello " +
//					"Antonio Apollinare " +
//					"Apollo Apuleio " +
//					"Aquilino Araldo " +
//					"Aratone Arcadio " +
//					"Archimede Archippo " +
//					"Arcibaldo Ardito " +
//					"Arduino Aresio " +
//					"Argimiro Argo " +
//					"Arialdo Ariberto " +
//					"Ariele Ariosto " +
//					"Aris Aristarco " +
//					"Aristeo Aristide " +
//					"Aristione Aristo " +
//					"Aristofane Aristotele " +
//					"Armando Arminio " +
//					"Arnaldo Aronne " +
//					"Arrigo Arturo " +
//					"Ascanio Asdrubale " +
//					"Asimodeo Assunto " +
//					"Asterio Astianatte " +
//					"Ataleo Atanasio " +
//					"Athos Attila " +
//					"Attilano Attilio " +
//					"Auberto Audace " +
//					"Augusto Aureliano " +
//					"Aurelio Auro " +
//					"Ausilio Averardo " +
//					"Azeglio Azelio " +
//					"Caino Caio " +
//					"Calanico Calcedonio " +
//					"Callisto Calogero " +
//					"Camillo Candido " +
//					"Cantidio Canziano " +
//					"Carlo Carmelo " +
//					"Carmine Caronte " +
//					"Carponio Casimiro " +
//					"Cassiano Cassio " +
//					"Casto Cataldo " +
//					"Catullo Cecco " +
//					"Cecilio Celso " +
//					"Cesare Cesario " +
//					"Cherubino Chiaffredo " +
//					"Cino Cipriano " +
//					"Cirano Ciriaco " +
//					"Cirillo Cirino " +
//					"Ciro Clarenzio " +
//					"Claudio Cleandro " +
//					"Clemente Cleonico " +
//					"Climaco Clinio " +
//					"Clodomiro Clodoveo " +
//					"Colmanno Colmazio " +
//					"Colombano Colombo " +
//					"Concetto Concordio " +
//					"Corbiniano Coreno " +
//					"Coriolano Cornelio " +
//					"Coronato Corrado " +
//					"Cosimo Cosma " +
//					"Costante Costantino " +
//					"Costanzo Cremenzio " +
//					"Crescente Crescenzio " +
//					"Crespignano Crispino " +
//					"Cristaldo Cristiano " +
//					"Cristoforo Crocefisso " +
//					"Cuniberto Cupido " +
//					"Macario Maccabeo " +
//					"Maffeo Maggiorino " +
//					"Magno Maiorico " +
//					"Malco Mamante " +
//					"Mancio Manetto " +
//					"Manfredo Manilio " +
//					"Manlio Mansueto " +
//					"Manuele Marcello " +
//					"Marciano Marco " +
//					"Mariano Marino " +
//					"Mario Marolo " +
//					"Martino Marzio " +
//					"Massimiliano Massimo " +
//					"Matroniano Matteo " +
//					"Mattia Maurilio " +
//					"Maurizio Mauro " +
//					"Medardo Medoro " +
//					"Melanio Melchiade " +
//					"Melchiorre Melezio " +
//					"Menardo Menelao " +
//					"Meneo Mennone " +
//					"Mercurio Metello " +
//					"Metrofane Michelangelo " +
//					"Michele Milo " +
//					"Minervino Mirco " +
//					"Mirko Mirocleto " +
//					"Misaele Modesto " +
//					"Monaldo Monitore " +
//					"Moreno Mosè " +
//					"Muziano " +
//					"Namazio Napoleone " +
//					"Narciso Narseo " +
//					"Narsete Natale " +
//					"Nazario Nazzareno " +
//					"Nazzaro Neopolo " +
//					"Neoterio Nereo " +
//					"Neri Nestore " +
//					"Nicarete Nicea " +
//					"Niceforo Niceto " +
//					"Nicezio Nico " +
//					"Nicodemo Nicola " +
//					"Nicolò Niniano " +
//					"Nino Noè " +
//					"Norberto Nostriano " +
//					"Nunzio Oddone " +
//					"Oderico Odidone " +
//					"Odorico Olimpio " +
//					"Olindo Oliviero " +
//					"Omar Omero " +
//					"Onesto Onofrio " +
//					"Onorino Onorio " +
//					"Orazio Orenzio " +
//					"Oreste Orfeo " +
//					"Orio Orlando " +
//					"Oronzo Orsino " +
//					"Orso Ortensio " +
//					"Oscar Osmondo " +
//					"Osvaldo Otello " +
//					"Ottaviano Ottavio " +
//					"Ottone Ovidio " +
//					"Paciano Pacifico " +
//					"Pacomio Palatino " +
//					"Palladio Pammachio " +
//					"Pancario Pancrazio " +
//					"Panfilo Pantaleo " +
//					"Pantaleone Paolo " +
//					"Pardo Paride " +
//					"Parmenio Pasquale " +
//					"Paterniano Patrizio " +
//					"Patroclo Pauside " +
//					"Peleo Pellegrino " +
//					"Pericle Perseo " +
//					"Petronio Pierangelo " +
//					"Piergiorgio Pierluigi " +
//					"Piermarco Piero " +
//					"Piersilvio Pietro " +
//					"Pio Pippo " +
//					"Placido Platone " +
//					"Plinio Plutarco " +
//					"Polidoro Polifemo " +
//					"Pollione Pompeo " +
//					"Pomponio Ponziano " +
//					"Ponzio Porfirio " +
//					"Porziano Postumio " +
//					"Prassede Priamo " +
//					"Primo Prisco " +
//					"Privato Procopio " +
//					"Prospero Protasio " +
//					"Proteo Prudenzio " +
//					"Publio Pupolo " +
//					"Pusicio Quarto " +
//					"Quasimodo Querano " +
//					"Quintiliano Quintilio " +
//					"Quintino Quinziano " +
//					"Quinzio Quirino " +
//					"Taddeo Taide " +
//					"Tammaro Tancredi " +
//					"Tarcisio Tarso " +
//					"Taziano Tazio " +
//					"Telchide Telemaco " +
//					"Temistocle Teobaldo " +
//					"Teodoro Teodosio " +
//					"Teodoto Teogene " +
//					"Terenzio Terzo " +
//					"Tesauro Tesifonte " +
//					"Tibaldo Tiberio " +
//					"Tiburzio Ticone " +
//					"Timoteo Tirone " +
//					"Tito Tiziano " +
//					"Tizio Tobia " +
//					"Tolomeo Tommaso " +
//					"Torquato Tosco " +
//					"Tranquillo Tristano " +
//					"Tulliano Tullio " +
//					"Turi Turibio " +
//					"Tussio Ubaldo " +
//					"Ubertino Uberto " +
//					"Ugo Ugolino " +
//					"Uguccione Ulberto " +
//					"Ulderico Ulfo " +
//					"Ulisse Ulpiano " +
//					"Ulrico Ulstano " +
//					"Ultimo Umberto " +
//					"Umile Uranio " +
//					"Urbano Urdino " +
//					"Uriele Ursicio " +
//					"Ursino Ursmaro " +
//					"Valente Valentino " +
//					"Valeriano Valerico " +
//					"Valerio Valfredo " +
//					"Valfrido Valtena " +
//					"Valter Varo " +
//					"Vasco Vedasto " +
//					"Velio Venanzio " +
//					"Venceslao Venerando " +
//					"Venerio Ventura " +
//					"Venustiano Venusto " +
//					"Verano Verecondo " +
//					"Verenzio Verulo " +
//					"Vespasiano Vezio " +
//					"Vidiano Vidone " +
//					"Vilfredo Viliberto " +
//					"Vincenzo Vindonio " +
//					"Vinebaldo Vinfrido " +
//					"Vinicio Virgilio " +
//					"Virginio Virone " +
//					"Viscardo Vitale " +
//					"Vitalico Vito " +
//					"Vittore Vittoriano " +
//					"Vittorio Vivaldo " +
//					"Viviano Vladimiro " +
//					"Vodingo Volfango " +
//					"Vulmaro Vulpiano " +
//					"Tabita Tamara " +
//					"Tarquinia Tarsilla " +
//					"Taziana Tea " +
//					"Tecla Telica " +
//					"Teodata Teodolinda " +
//					"Teodora Teresa " +
//					"Teudosia Tina " +
//					"Tiziana Tosca " +
//					"Trasea Tullia " +
//					"Ugolina Ulfa " +
//					"Uliva Unna " +
//					"Vala Valentina " +
//					"Valeria Valeriana " +
//					"Vanda Vanessa " +
//					"Vanna Venera " +
//					"Veneranda Venere " +
//					"Venusta Vera " +
//					"Verdiana Verena " +
//					"Veriana Veridiana " +
//					"Veronica Viliana " +
//					"Vilma Vincenza " +
//					"Viola Violante " +
//					"Virginia Vissia " +
//					"Vittoria Viviana " +
//					"Palladia Palmazio " +
//					"Palmira Pamela " +
//					"Paola Patrizia " +
//					"Pelagia Penelope " +
//					"Perla Petronilla " +
//					"Pia Piera " +
//					"Placida Polissena " +
//					"Porzia Prisca " +
//					"Priscilla Proserpina " +
//					"Prospera Prudenzia " +
//					"Quartilla Quieta " +
//					"Quiteria " +
//					"Calogera Calpurnia " +
//					"Camelia Camilla " +
//					"Candida Capitolina " +
//					"Carina Carla " +
//					"Carlotta Carmela " +
//					"Carmen Carola " +
//					"Carolina Casilda " +
//					"Casimira Cassandra " +
//					"Cassiopea Catena " +
//					"Caterina Cecilia " +
//					"Celeste Celinia " +
//					"Chiara Cinzia " +
//					"Cirilla Clara " +
//					"Claudia Clelia " +
//					"Clemenzia Cleo " +
//					"Cleofe Cleopatra " +
//					"Cloe Clorinda " +
//					"Cointa Colomba " +
//					"Concetta Consolata " +
//					"Cora Cordelia " +
//					"Corinna Cornelia " +
//					"Cosima Costanza " +
//					"Crescenzia Cristiana " +
//					"Cristina Crocefissa " +
//					"Cronida Cunegonda " +
//					"Cuzia " +
//					"Gabriella Gaia " +
//					"Galatea Gaudenzia " +
//					"Gelsomina Geltrude " +
//					"Gemma Generosa " +
//					"Genesia Genoveffa " +
//					"Germana Gertrude " +
//					"Ghita Giacinta " +
//					"Giada Gigliola " +
//					"Gilda Giliola " +
//					"Ginevra Gioacchina " +
//					"Gioconda Gioia " +
//					"Giorgia Giovanna " +
//					"Gisella Giuditta " +
//					"Giulia Giuliana " +
//					"Giulitta Giuseppa " +
//					"Giuseppina Giusta " +
//					"Glenda Gloria " +
//					"Godeberta Godiva " +
//					"Grazia Graziana " +
//					"Graziella Greta " +
					"Griselda Guenda " +
					"Guendalina Gundelinda").split(' ')
	}

	fun generatePosts(limit: Int = 5) {
		val posts = mutableListOf<Post>()
		val users = mutableListOf<User>()
		val tags = DataManager.tags?.toList()?: listOf()
		for (i in 1..limit) {
			val user = User(
				name = names.names.random(),
				surname = names.names.random(),
				gender = Gender.values().random(),
				cellNumber = RandomManager.getRandomCellNumber(),
				instagramNickname = RandomManager.getRandomString(8),
				tags = tags.randomList(4).toMutableList(),
			)
			user.uid = RandomManager.getRandomString(28)
			users.add(user)
			for (j in 1..Random.nextInt(4)) {
				posts.add(
					Post(
						authorUID = user.uid,
						location = Locations.values().random(),
						gender = Gender.values().random(),
						tags = tags.randomList(8).toMutableList(),
						date = Calendar.getInstance().time.addDays((-355..0).random())
					)
				)
			}
		}
		users.forEach { DatabaseManager.put("users/${it.uid}", it) }
		posts.forEach { DatabaseManager.post("posts", it) }
	}

	fun generateTags(context: Context): Set<Tag> {
		return setOf(

			//Height
			Tag("Alto", context.getString(R.string.HumanMaleHeightVariant)),
			Tag("Basso", context.getString(R.string.HumanMaleHeightVariant)),

			//Hair
			Tag("Ricci", context.getString(R.string.HairDryer)),
			Tag("Lisci", context.getString(R.string.HairDryer)),

			Tag("Felpa", context.getString(R.string.TshirtCrew)),
			Tag("Camicia", context.getString(R.string.TshirtCrew)),
			Tag("Giubbotto", context.getString(R.string.TshirtCrew)),
			Tag("Cardigan", context.getString(R.string.TshirtCrew)),

			Tag("Da vista", context.getString(R.string.Sunglasses)),
			Tag("Da sole", context.getString(R.string.Sunglasses)),
		)
	}
}