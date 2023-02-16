# DeveloperJokesApp

## Background
In order for new developers to learn how to use various API's, we've previously worked with various open API's that are open for public use.
Due to changes in other API's, or by the people represented acting up. We created a new API service that just returns simple jokes. 

## Technology
The application is built with Kotlin using Spring Boot and Gradle build tools. With PostgreSQL for the DB. 

## How to use
Simply call the endpoint `{where ever it's running}/api/jokes/random` with a GET, and you'll get a random joke. 
 - `/api/jokes` will return all jokes
 - `/api/jokes/{id}` will return the joke with that ID. 
 
 You can also use the `language` param on `/api/jokes/random` and `/api/jokes`, and it will filter the jokes by language.
 > example: `/api/jokes/random?language=se` will return a random joke in Swedish! 
 
 ## Roadmap
 Will add more stuff as we go along. 
