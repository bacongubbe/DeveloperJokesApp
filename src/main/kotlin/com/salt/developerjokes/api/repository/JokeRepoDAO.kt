package com.salt.developerjokes.api.repository

import com.salt.developerjokes.api.model.jokes.Joke
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.random.Random

private fun List<Joke>.getRandom(): Joke? {
  if (this.isEmpty()) {
    return null
  }
  return this[Random.nextInt(this.size)]
}

private fun String.toUUID(): UUID = UUID.fromString(this)

@Repository
class JokeRepoDAO(@Autowired private val repo: IJokeRepoDAO) {

  private final val logger = LoggerFactory.getLogger(JokeRepoDAO::class.java)

  private final val UUID_PATTERN =
    Regex("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$")

  fun saveJoke(joke: Joke): Joke = repo.save(joke)

  fun getAllJokes(): List<Joke> = repo.findAll().filterNotNull()

  fun getAllJokesForLanguage(lang: String): List<Joke> = repo.findAllByLanguage(lang)

  fun getJoke(id: String): Joke? {
    validateUUID(id)
    return repo.findById(id.toUUID()).orElse(null)
  }

  fun deleteJoke(id: String) {
    validateUUID(id)
    try {
      repo.deleteById(id.toUUID())
    } catch (exception: EmptyResultDataAccessException) {
      logger.warn("user tried to delete: $id, but it was not found in the database")
    }
  }

  fun deleteJoke(joke: Joke) = repo.delete(joke)

  fun getRandomJoke(): Joke? = getAllJokes().getRandom()

  fun getRandomJoke(lang: String): Joke? = getAllJokesForLanguage(lang).getRandom()

  private fun validateUUID(id: String) {
    if (!id.matches(UUID_PATTERN)) {
      throw ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid ID")
    }
  }
}
