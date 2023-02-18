package com.salt.developerjokes.api

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

private val log = LoggerFactory.getLogger(SecurityConfig::class.java)

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Value("\${admin.user}")
  lateinit var admin : String

  @Value("\${admin.password}")
  lateinit var password : String

  @Bean
  fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http.csrf().disable()
      .authorizeHttpRequests()
      .requestMatchers("/login").permitAll()
      .requestMatchers(HttpMethod.GET, "/**").permitAll()
      .requestMatchers(HttpMethod.POST, "/**").authenticated()
      .requestMatchers(HttpMethod.PUT, "/**").authenticated()
      .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
      .and().formLogin()
      .successHandler { _, _, authentication -> log.info("User ${authentication.name} logged in successfully") }
      .and().httpBasic()
      .and().build()
  }

  @Bean
  @Profile("!test")
  fun userDetailsManager(encoder: PasswordEncoder): InMemoryUserDetailsManager {
    val user = User.withUsername(admin)
      .password(encoder.encode(password)).roles("USER", "ADMIN").build()
    return InMemoryUserDetailsManager(user)
  }

  @Bean
  fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
