package com.salt.developerjokes.api.DeveloperJokesApp.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.server.ResponseStatusException

@SpringBootTest
class JokeRepositoryTest(@Autowired val repo: JokeRepository) {

  @Test
  fun getRandomJoke() {
    assertNotNull(repo.getRandomJoke())
  }

  @Test
  fun getSpecificJoke() {
    val expected = "Why do Java programmers have to wear glasses? Because they don’t C#."
    val actual = repo.getSpecificJoke("1dee7653-1adf-4be4-8c5c-ec74b563b0eb")
    assertEquals(expected, actual.text)
  }

  @Test
  fun shouldThrowForNonExistingJoke() {
    val exception =
      assertThrows(ResponseStatusException::class.java) { repo.getSpecificJoke("1dee7653-1adf-4be4-8c5c-ec74b563b0ea") }
    assertEquals("Joke not found", exception.reason)
  }

  @Test
  fun shouldThrowExceptionIfIdIsInvalid() {
    assertThrows(ResponseStatusException::class.java) { repo.getSpecificJoke("000") }
  }
}
