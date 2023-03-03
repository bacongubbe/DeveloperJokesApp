package com.salt.developerjokes.api.model.repository

import com.salt.developerjokes.api.model.jokes.Joke
import com.salt.developerjokes.api.repository.JokeRepoDAO
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.util.*

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class JokeRepoDAOTest(@Autowired val repo : JokeRepoDAO) {

  @Test
  @Order(0)
  fun shouldBeAbleToSaveJoke(){
    val expected = Joke("testerJoke", UUID.fromString("a048f9e6-bb02-481d-9246-3464dd4a187b"), "en")
    val actual = repo.saveJoke(expected)
    assertEquals(expected, actual)
  }

  @Test
  @Order(1)
  fun shouldBeAbleToSaveMultipleJokes(){
    val expected = 5
    repo.saveJoke(Joke("SecondJoke", UUID.randomUUID(), "en"))
    repo.saveJoke(Joke("ThirdJoke", UUID.randomUUID(), "se"))
    repo.saveJoke(Joke("FourthJoke", UUID.randomUUID(), "se"))
    repo.saveJoke(Joke("FifthJoke", UUID.randomUUID(), "se"))
    val actual = repo.getAllJokes().size
    assertEquals(expected,actual)
  }

  @Test
  @Order(2)
  fun shouldBeAbleToFindByLanguage(){
    val expected = 2
    val actual = repo.getAllJokesForLanguage("en").size
    assertEquals(expected,actual)
  }

  @Test
  @Order(3)
  fun shouldBeAbleToGetJokeById(){
    val expected = repo.saveJoke(Joke("JokeWithId", UUID.randomUUID(), "en"))
    val actual = repo.getJoke(expected.id.toString())
    assertEquals(expected, actual)
  }

  @Test
  @Order(4)
  fun shouldReturnNullForInvalidId(){
    assertNull(repo.getJoke(UUID.randomUUID().toString()))
  }

  @Test
  @Order(5)
  fun shouldReturnEmptyListForUnknownLanguage(){
    assertEquals(0, repo.getAllJokesForLanguage("fake").size)
  }

  @Test
  @Order(6)
  fun shouldUpdateIfPrimaryKeyIsSame(){
    val newJoke = Joke("testerJoke", UUID.fromString("a048f9e6-bb02-481d-9246-3464dd4a187b"), "en")
    val updatedJoke = repo.saveJoke(newJoke)
    assertEquals(updatedJoke.text, newJoke.text)
    assertEquals(6, repo.getAllJokes().size)
  }

  @Test
  @Order(7)
  fun shouldGetRandomJoke(){
    val jokes = mutableSetOf<Joke>()
    repeat(30){ repo.getRandomJoke()?.let { jokes.add(it) } }
    assertTrue(jokes.size > 1)
  }

  @Test
  @Order(8)
  fun shouldGetRandomJokeForLanguage(){
    val jokes = mutableSetOf<Joke>()
    repeat(30){ repo.getRandomJoke("se")?.let { jokes.add(it) } }
    val englishJokes = jokes.filter { it.language == "en" }
    val swedishJokes = jokes.filter { it.language == "se" }
    assertEquals(0, englishJokes.size)
    assertTrue(swedishJokes.size > 1)
  }

  @Test
  @Order(9)
  fun shouldDeleteById(){
    assertEquals(6, repo.getAllJokes().size)
    repo.deleteJoke("a048f9e6-bb02-481d-9246-3464dd4a187b")
    assertEquals(5, repo.getAllJokes().size)
  }

  @Test
  @Order(10)
  fun shouldDeleteByObject(){
    assertEquals(5, repo.getAllJokes().size)
    repo.getRandomJoke()?.let { repo.deleteJoke(it) }
    assertEquals(4, repo.getAllJokes().size)
  }

  @Test
  @Order(11)
  fun addingSameJokeTwiceShouldNotAddNewJoke(){
    assertEquals(4, repo.getAllJokes().size)
    val joke1 = repo.getRandomJoke()
    val joke2 = joke1?.id?.toString()?.let { repo.getJoke(it) }
    joke1?.let { repo.saveJoke(joke2!!) }
    assertEquals(4, repo.getAllJokes().size)
  }

}

