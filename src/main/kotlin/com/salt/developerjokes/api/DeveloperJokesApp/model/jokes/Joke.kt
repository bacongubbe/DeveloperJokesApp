package com.salt.developerjokes.api.DeveloperJokesApp.model.jokes

import java.util.*

class Joke(val text : String, val id : UUID, val language : String) {
  fun toDTO() : JokeDTO = JokeDTO(this.text, this.id.toString())
}
