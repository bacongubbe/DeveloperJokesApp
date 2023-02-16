package com.salt.developerjokes.api.DeveloperJokesApp.model.repository

import com.salt.developerjokes.api.DeveloperJokesApp.model.jokes.Joke
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.random.Random

private fun List<Joke>.getRandom() : Joke = this[Random.nextInt(this.size)]
private fun String.toUUID() : UUID? = UUID.fromString(this)

@Repository
class JokeRepository {

  private final val repo = mutableListOf<Joke>()
  private final val UUID_PATTERN = Regex("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$")

  init {
      repo += listOf(
          Joke(
              "Why are Assembly programmers always soaking wet? They work below C-level!",
              UUID.fromString("7eba7875-c2fc-42d3-a920-559bfc83f2bf"), "en"
          ),
          Joke(
              "Why do Java programmers have to wear glasses? Because they don’t C#.",
              UUID.fromString("1dee7653-1adf-4be4-8c5c-ec74b563b0eb"), "en"
          ),
          Joke(
              "Varför har Javautvecklare glasögon? De kan inte C",
              UUID.fromString("b55a8632-bb9e-4c9d-9581-8b474fce8878"), "se"
          ),
          Joke(
              "Jag är specialist på Microsofts alkoholprogram, alltså en systemvetare.",
              UUID.fromString("19b0f9ef-c09b-4cbd-a604-07dffdc16d5c"), "se"
          )
      )
  }
  fun getRandomJoke() : Joke = repo.getRandom()

  fun getSpecificJoke(id : String ) : Joke {
    validateUUID(id)
    return repo.stream().filter { it.id == id.toUUID() }.findFirst()
      .orElseThrow { throw ResponseStatusException(HttpStatusCode.valueOf(404), "Joke not found") }
  }

  fun getRandomJokeForLanguage(lang : String) : Joke = repo.stream().filter { it.language == lang }.toList().getRandom()

  private fun validateUUID(id : String) {
    if (!id.matches(UUID_PATTERN)){
      throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid ID")
    }
  }
}
