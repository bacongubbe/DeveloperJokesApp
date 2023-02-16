package com.salt.developerjokes.api.DeveloperJokesApp.model.repository

import com.salt.developerjokes.api.DeveloperJokesApp.model.jokes.Joke
import org.springframework.data.repository.CrudRepository
import java.util.*

interface IJokeRepoDAO : CrudRepository<Joke, UUID> {
}
