# DeveloperJokesApp
[![codecov](https://codecov.io/gh/bacongubbe/DeveloperJokesApp/branch/main/graph/badge.svg)](https://codecov.io/gh/bacongubbe/DeveloperJokesApp)

See the deployed API [here!](https://jokes.bacongubbe.dev/api/jokes/random) 

## Disclaimer
The development and maintanace of the application has been moved over to / adopted by @appliedtechnology in a private repository. 

## Background

In order for new developers to learn how to use various API's, we've previously worked with various open API's that are
open for public use.
Due to changes in other API's, or by the people represented acting up. We created a new API service that just returns
simple jokes.

## Stack
[![Kotlin][Kotlin_logo]][Kotlin_url]
[![Spring][Spring_logo]][Spring_url]
[![PostgreSQL][PostgreSQL_logo]][PostgreS_url]
[![AWS][AWS_logo]][AWS_url]

## How to use (public)

Simply call the endpoint `{where ever it's running}/api/jokes/random` with a GET, and you'll get a random joke.

- `/api/jokes` will return all jokes
- `/api/jokes/{id}` will return the joke with that ID.

You can also use the `language` param on `/api/jokes/random` and `/api/jokes`, and it will filter the jokes by language.
> example: `/api/jokes/random?language=se` will return a random joke in Swedish!

## How to use (admin)

In order to perform any operations except from GETting jokes. You'll need to authenticate yourself. Do this by adding basic authentication 
in the API tool you're using. Reach out to me if you're not aware of the admin details. You can do it either a API tool like [Postman](https://www.postman.com/), or by using the console in your browser. 

### Adding a joke

In order to add a joke to the Database, issue a `POST` request to `/api/jokes` with a Joke in the body, with the following
format:

```json
{
  "text" : "{funny joke}",
  "language" : "en"
}
```
This will create a new joke and assign a new random UUID, you'll get the id in a response 201 location header.

### Deleting a joke

In order to delete a joke, simply make a `DELETE` request to `api/jokes/{id}`.

### Updating a joke

In order to update a Joke, send a `PUT` request to `api/jokes/{id}` with the updated version of the joke in the body (like te `POST` request) and the joke will be updated. 
Note that `"language"` has to be present. 

## Roadmap

Swagger is not applicable since there's no current version that supports this application. 
Upcoming is a UI to actually interact with the backend for administration. 


[//]: # (Variables)
[AWS_logo]: https://img.shields.io/badge/aws-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white
[AWS_url]: https://aws.amazon.com/

[Kotlin_logo]: https://img.shields.io/badge/kotlin-A020F0?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin_url]: https://kotlinlang.org/

[Spring_logo]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring_url]: https://spring.io/projects/spring-boot

[PostgreSQL_logo]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreS_url]: https://www.postgresql.org/
