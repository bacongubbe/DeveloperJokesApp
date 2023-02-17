package com.salt.developerjokes.api.model.jokes

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

  constructor(joke : IncomingJokeDTO) : this(joke.text,UUID.randomUUID(), joke.language)

  fun toDTO(): JokeDTO = JokeDTO(this.text, this.id.toString())
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Joke

    if (text != other.text) return false
    if (id != other.id) return false
    if (language != other.language) return false

    return true
  }

  override fun hashCode(): Int {
    var result = text.hashCode()
    result = 31 * result + id.hashCode()
    result = 31 * result + language.hashCode()
    return result
  }
}
