package com.salt.developerjokes.api.DeveloperJokesApp.model.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JokeRepoDAO {

  @Autowired
  lateinit var repo : IJokeRepoDAO

}
