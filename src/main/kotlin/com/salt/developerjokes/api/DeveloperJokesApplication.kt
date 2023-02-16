package com.salt.developerjokes.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val logger : Logger = LoggerFactory.getLogger(DeveloperJokesApplication::class.java)
@SpringBootApplication
class DeveloperJokesApplication
fun main(args: Array<String>) {
  logger.info("Application starting")
	runApplication<DeveloperJokesApplication>(*args)
}

