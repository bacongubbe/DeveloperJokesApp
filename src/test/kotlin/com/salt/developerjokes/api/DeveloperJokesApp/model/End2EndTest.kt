package com.salt.developerjokes.api.DeveloperJokesApp.model;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class End2EndTest(@Autowired val client : WebTestClient, @Value("\${server.port}") val DEFAULT_PORT : String) {

  final val BASE_URL = "http://localhost:$DEFAULT_PORT/api/jokes"

  @Test
  fun shouldConfigurePort(){
    assertEquals("9999", DEFAULT_PORT)
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
      .expectBody().jsonPath("message").isEqualTo("Joke not found")
  }

  @Test
  fun shouldReturn400WithMessageForInvalidId(){
    client.get().uri("$BASE_URL/000")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().is4xxClientError
      .expectBody().jsonPath("message").isEqualTo("Invalid ID")
  }
}
