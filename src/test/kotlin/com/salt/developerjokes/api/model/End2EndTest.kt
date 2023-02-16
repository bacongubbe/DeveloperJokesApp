package com.salt.developerjokes.api.model

import com.salt.developerjokes.api.model.jokes.IncomingJokeDTO
import com.salt.developerjokes.api.model.jokes.Joke
import com.salt.developerjokes.api.repository.JokeRepoDAO
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class End2EndTest(@Autowired val client : WebTestClient, @Value("\${server.port}") val DEFAULT_PORT : String, @Autowired val repo : JokeRepoDAO) {

  final val BASE_URL = "http://localhost:$DEFAULT_PORT/api/jokes"

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
    repo.deleteJoke("b55a8632-bb9e-4c9d-9581-8b474fce8879")
  }

  @Test
  fun shouldGetRandomJoke(){
    client.get().uri("$BASE_URL/random").accept(MediaType.APPLICATION_JSON)
      .exchange().expectStatus().isOk
      .expectBody().jsonPath("text").exists()
  }

  @Test
  fun shouldGetSpecificJoke(){
    client.get().uri("$BASE_URL/1dee7653-1adf-4be4-8c5c-ec74b563b0eb").accept(MediaType.APPLICATION_JSON)
      .exchange().expectStatus().isOk
      .expectBody().jsonPath("text")
      .isEqualTo("Why do Java programmers have to wear glasses? Because they don’t C#.")
  }

  @Test
  fun shouldReturn404WithMessageForNonExistingId(){
    client.get().uri("$BASE_URL/1dee7653-1adf-4be4-8c5c-ec74b563b0ea")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound
      .expectBody().jsonPath("message").isEqualTo("No Joke with that ID")
  }

  @Test
  fun shouldReturn400WithMessageForInvalidId(){
    client.get().uri("$BASE_URL/000")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError
      .expectBody().jsonPath("message").isEqualTo("Invalid ID")
  }

  @Test
  fun shouldReturnLocationHeaderForPost(){
    val id = "b55a8632-bb9e-4c9d-9581-8b474fce8879"
    client.post().uri(BASE_URL).bodyValue(IncomingJokeDTO("test",id,"en"))
      .exchange()
      .expectStatus().isCreated
      .expectHeader().location("/api/jokes/$id")
  }
}
