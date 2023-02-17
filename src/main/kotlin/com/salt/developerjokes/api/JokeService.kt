package com.salt.developerjokes.api

import com.salt.developerjokes.api.model.jokes.IncomingJokeDTO
import com.salt.developerjokes.api.model.jokes.Joke
import com.salt.developerjokes.api.model.jokes.JokeDTO
import com.salt.developerjokes.api.model.jokes.JokeListDTO
import com.salt.developerjokes.api.repository.JokeRepoDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class JokeService(@Autowired private val repo: JokeRepoDAO) {
  fun getRandomJoke(): JokeDTO = repo.getRandomJoke()?.toDTO() ?: throw ResponseStatusException(
    HttpStatusCode.valueOf(404),
    "No Jokes in the database"
  )

  fun getRandomJoke(language: String): JokeDTO = repo.getRandomJoke(language)?.toDTO() ?: throw ResponseStatusException(
    HttpStatusCode.valueOf(404),
    "No Jokes for that language"
  )

  fun getSpecificJoke(id: String): JokeDTO =
    repo.getJoke(id)?.toDTO() ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "No Joke with that ID")

  fun getAllJokes(): JokeListDTO = JokeListDTO(repo.getAllJokes().map { it.toDTO() })

  fun getAllJokes(language: String): JokeListDTO = JokeListDTO(repo.getAllJokesForLanguage(language).map { it.toDTO() })

  fun createNewJoke(joke: IncomingJokeDTO): JokeDTO = repo.saveJoke(Joke(joke)).toDTO()

  fun updateJoke(id: String, newJoke: IncomingJokeDTO): JokeDTO {
    val oldJoke =
      repo.getJoke(id) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "No Joke found to update")
    return repo.saveJoke(Joke(oldJoke, newJoke)).toDTO()
  }

  fun deleteJoke(id: String) = repo.deleteJoke(id)
}
