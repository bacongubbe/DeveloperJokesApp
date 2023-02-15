package com.salt.developerjokes.api.DeveloperJokesApp.model

import com.salt.developerjokes.api.DeveloperJokesApp.model.jokes.JokeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/jokes")
class Controller(@Autowired val service: JokeService) {

  @GetMapping("/random")
  fun getRandomJoke(@RequestParam language : String?) : ResponseEntity<JokeDTO> {
    if (language == null){
      return ResponseEntity.ok(service.getRandomJoke())
    }
    return ResponseEntity.ok(service.getRandomJoke(language))
  }

  @GetMapping("/{id}")
  fun getSpecificJoke(@PathVariable id : String) : ResponseEntity<JokeDTO> = ResponseEntity.ok(service.getSpecificJoke(id))
}
