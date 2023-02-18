package com.salt.developerjokes.api

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication

@Configuration
@EnableWebSecurity
class TestConfig {

  val admin = "testAdmin"
  val password = "testPassword"

  @Bean
  fun webTestClient() : WebTestClient{
    return WebTestClient.bindToServer()
      .filter(basicAuthentication(admin, password))
      .build()
  }

  @Bean
  fun testRestTemplate() : TestRestTemplate{
    return TestRestTemplate().withBasicAuth(admin, password)
  }

  @Bean
  @Profile("test")
  fun userTestDetailsManager(encoder: PasswordEncoder): InMemoryUserDetailsManager {
    println("HERE IS THE THING")
    val user = User.withUsername(admin)
      .password(encoder.encode(password)).roles("USER", "ADMIN").build()
    return InMemoryUserDetailsManager(user)
  }

}
