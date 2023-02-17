package com.salt.developerjokes.api

import com.salt.developerjokes.api.model.jokes.IncomingJokeDTO
import com.salt.developerjokes.api.model.jokes.JokeDTO
import com.salt.developerjokes.api.model.jokes.JokeListDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/jokes")
class JokeController(@Autowired val service: JokeService) {

  @GetMapping
  fun getAllJokes(@RequestParam(required = false) language: String?): ResponseEntity<JokeListDTO> {
    if (language == null || language == "") {
      return ResponseEntity.ok(service.getAllJokes())
    }
    return ResponseEntity.ok(service.getAllJokes(language))
  }

  @GetMapping("/random")
  fun getRandomJoke(@RequestParam language: String?): ResponseEntity<JokeDTO> {
    if (language == null || language == "") {
      return ResponseEntity.ok(service.getRandomJoke())
    }
    return ResponseEntity.ok(service.getRandomJoke(language))
  }

  @GetMapping("/{id}")
  fun getSpecificJoke(@PathVariable id: String): ResponseEntity<JokeDTO> =
    ResponseEntity.ok(service.getSpecificJoke(id))

  @PostMapping
  fun addJoke(@RequestBody joke: IncomingJokeDTO): ResponseEntity<Nothing> {
    val newJoke = service.createNewJoke(joke)
    return ResponseEntity.created(URI.create("/" + newJoke.id)).build()
  }

  @PutMapping("/{id}")
  fun updateJoke(@RequestBody joke: IncomingJokeDTO, @PathVariable id: String): ResponseEntity<JokeDTO> {
    return ResponseEntity.ok(service.updateJoke(id, joke))
  }

  @DeleteMapping("/{id}")
  fun deleteJoke(@PathVariable id: String): ResponseEntity<Nothing> {
    service.deleteJoke(id)
    return ResponseEntity.noContent().build()
  }
}
