package com.salt.developerjokes.api.repository

import com.salt.developerjokes.api.model.jokes.Joke
import org.springframework.data.repository.CrudRepository
import java.util.*

interface IJokeRepoDAO : CrudRepository<Joke, UUID> {
  fun findAllByLanguage(lang: String): List<Joke>
}
