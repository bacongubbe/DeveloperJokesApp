package com.salt.developerjokes.api.model

import com.salt.developerjokes.api.model.jokes.IncomingJokeDTO
import com.salt.developerjokes.api.model.jokes.Joke
import com.salt.developerjokes.api.repository.JokeRepoDAO
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class End2EndTest(
  @Value("\${server.port}") val DEFAULT_PORT : String,
  @Autowired val repo : JokeRepoDAO) {

  final val BASE_URL = "http://localhost:$DEFAULT_PORT/api/jokes"
  @Autowired
  lateinit var client: WebTestClient

  @Autowired
  lateinit var restTemplate: TestRestTemplate

  @Test
  fun shouldConfigurePort(){
    assertEquals("9999", DEFAULT_PORT)
  }

  @BeforeAll
  fun setUp(){
    repo.saveJoke(Joke("Why are Assembly programmers always soaking wet? They work below C-level!",
      UUID.fromString("7eba7875-c2fc-42d3-a920-559bfc83f2bf"), "en"))
    repo.saveJoke(Joke("Why do Java programmers have to wear glasses? Because they don’t C#.",
      UUID.fromString("1dee7653-1adf-4be4-8c5c-ec74b563b0eb"), "en"))
    repo.saveJoke(Joke("Varför har Javautvecklare glasögon? De kan inte C",
      UUID.fromString("b55a8632-bb9e-4c9d-9581-8b474fce8878"), "se"))
    repo.saveJoke(Joke("Jag är specialist på Microsofts alkoholprogram, alltså en systemvetare.",
      UUID.fromString("19b0f9ef-c09b-4cbd-a604-07dffdc16d5c"), "se"))
  }

  @AfterAll
  fun tearDown(){
    repo.deleteJoke("7eba7875-c2fc-42d3-a920-559bfc83f2bf")
    repo.deleteJoke("1dee7653-1adf-4be4-8c5c-ec74b563b0eb")
    repo.deleteJoke("b55a8632-bb9e-4c9d-9581-8b474fce8878")
    repo.deleteJoke("19b0f9ef-c09b-4cbd-a604-07dffdc16d5c")
  }

  @Test
  @Order(0)
  fun shouldGetRandomJoke(){
    client.get().uri("$BASE_URL/random").accept(MediaType.APPLICATION_JSON)
      .exchange().expectStatus().isOk
      .expectBody().jsonPath("text").exists()
  }

  @Test
  @Order(1)
  fun shouldGetSpecificJoke(){
    client.get().uri("$BASE_URL/1dee7653-1adf-4be4-8c5c-ec74b563b0eb").accept(MediaType.APPLICATION_JSON)
      .exchange().expectStatus().isOk
      .expectBody().jsonPath("text")
      .isEqualTo("Why do Java programmers have to wear glasses? Because they don’t C#.")
  }

  @Test
  @Order(2)
  fun shouldReturn404WithMessageForNonExistingId(){
    client.get().uri("$BASE_URL/1dee7653-1adf-4be4-8c5c-ec74b563b0ea")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound
      .expectBody().jsonPath("message").isEqualTo("No Joke with that ID")
  }

  @Test
  @Order(3)
  fun shouldReturn400WithMessageForInvalidId(){
    client.get().uri("$BASE_URL/000")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError
      .expectBody().jsonPath("message").isEqualTo("Invalid ID")
  }
  @Test
  @Order(4)
  fun shouldReturnLocationHeaderForPost(){
    val exchange = restTemplate.exchange(BASE_URL, HttpMethod.POST,
      HttpEntity(IncomingJokeDTO("test", "en")), TestRestTemplate::class.java)

    client.get().uri(BASE_URL + exchange.headers.location.toString()).exchange().expectStatus().isOk
      .expectBody().jsonPath("text").isEqualTo("test")
  }

  @Test
  @Order(5)
  fun shouldBeAbleToUpdateJoke(){
    val testUri = "$BASE_URL/7eba7875-c2fc-42d3-a920-559bfc83f2bf"

    client.get().uri(testUri)
      .exchange().expectBody().jsonPath("text")
      .isEqualTo("Why are Assembly programmers always soaking wet? They work below C-level!")
    client.put().uri(testUri).bodyValue(IncomingJokeDTO("Test", "en"))
      .exchange().expectStatus().isOk
    client.get().uri(testUri).exchange().expectStatus().isOk
      .expectBody().jsonPath("text").isEqualTo("Test")
  }

  @Test
  @Order(6)
  fun shouldBeAbleToDeleteJoke() {
    val exchange = restTemplate.exchange(BASE_URL, HttpMethod.POST,
      HttpEntity(IncomingJokeDTO("ToBeDeleted", "en")), TestRestTemplate::class.java)

    client.delete().uri(BASE_URL + exchange.headers.location)
      .exchange().expectStatus().isNoContent
  }

  @Test
  @Order(7)
  fun deletingNonExistingJokeShouldReturn204(){
    client.delete().uri("$BASE_URL/" + UUID.randomUUID().toString())
      .exchange().expectStatus().isNoContent
  }

  @Test
  @Order(8)
  fun shouldReturnBadRequestForTryingToDeleteInvalidUUID(){
    client.delete().uri("$BASE_URL/000ABC").exchange()
      .expectStatus().isBadRequest
  }

  @Test
  @Order(9)
  fun shouldGetAllJokes(){
   client.get().uri(BASE_URL).exchange()
     .expectBody().jsonPath("result").value<List<String>> { assertEquals( it.size, 5) }
  }

  @Test
  @Order(10)
  fun shouldGetAllJokesForLanguageSE(){
    client.get().uri("$BASE_URL?language=se").exchange()
      .expectBody().jsonPath("result").value<List<String>>{ assertEquals(it.size, 2) }
  }

  @Test
  @Order(11)
  fun shouldReturnEmptyListForNonExistingLanguage(){
    client.get().uri("$BASE_URL?language=fake").exchange()
      .expectStatus().isOk.expectBody().jsonPath("result").value<List<String>> { assertEquals(it.size,0) }
  }

  @Test
  @Order(12)
  fun shouldGetRandomJokeForLanguage(){
    val list = mutableSetOf<String>()
    repeat(10) { client.get().uri("$BASE_URL/random?language=se").exchange()
      .expectBody().jsonPath("text").value<String> {  list.add(it) } }

    assertEquals(list.size, 2)
  }

  @Test
  @Order(13)
  fun shouldThrowIfNoJokesAvailableForLanguage(){
    client.get().uri("$BASE_URL/random?language=fake").exchange()
      .expectStatus().isNotFound
      .expectBody().jsonPath("message").isEqualTo("No Jokes for that language")
  }
}
