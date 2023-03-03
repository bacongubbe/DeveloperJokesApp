package com.salt.developerjokes.api.model.jokes

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class JokeTest {

  @Test
  fun shouldReturnTrueForTwoJokesWithSameValues() {
    val joke1 = Joke("TestJoke", UUID.randomUUID(), "en")
    val joke2 = Joke("TestJoke", joke1.id, "en")
    assertTrue(joke1 == joke2)
  }

  @Test
  fun shouldReturnFalseIfThereAreDifferencesInJoke(){
    val joke1 = Joke("TestJoke", UUID.randomUUID(), "en")
    val joke2 = Joke("TestJoke2", joke1.id, "en")
    assertFalse(joke1 == joke2)
  }

}

