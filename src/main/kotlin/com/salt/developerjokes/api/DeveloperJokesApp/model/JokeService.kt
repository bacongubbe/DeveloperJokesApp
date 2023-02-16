package com.salt.developerjokes.api.DeveloperJokesApp.model

import com.salt.developerjokes.api.DeveloperJokesApp.model.jokes.JokeDTO
import com.salt.developerjokes.api.DeveloperJokesApp.model.repository.JokeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JokeService(@Autowired private val repo: JokeRepository) {
  fun getRandomJoke() : JokeDTO = repo.getRandomJoke().toDTO()
  fun getRandomJoke(language : String) : JokeDTO = repo.getRandomJokeForLanguage(language).toDTO()
  fun getSpecificJoke(id : String) : JokeDTO = repo.getSpecificJoke(id).toDTO()
}
