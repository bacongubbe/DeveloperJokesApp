package com.salt.developerjokes.api.DeveloperJokesApp.model.jokes

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class Joke(
  val text: String,
  @Id @Column(name = "joke_id") val id: UUID,
  val language: String
) {
  fun toDTO(): JokeDTO = JokeDTO(this.text, this.id.toString())
}
