# Accounts service

## Description
This module provides current account, balances and transaction services.

## Dependencies
* Java 11
* Docker
* Postgres
* RabbitMQ
* Spring boot 2.4.5
* Gradle

# Getting Started
Build Jar
./gradlew clean build

### Running application with Docker and IntelliJ
1. Build docker image
`docker build --build-arg JAR_FILE=build/libs/\*.jar -t accountdemo .`

### Start the application containers:
1. Run `docker-compose up` to get all containers up(Springboot application, Rabbit, Postgres)
2. Check running containers `docker-compose up`
3. Run `bootrun` task from gradle sidebar or run `main` method from `Application.java` class.
   or `./gradlew bootRun`
4. Stop running container and remove image with `docker-compose down --rmi=local`

By default, service launches at http://localhost:9920

## Running tests
This project has  unit tests.

## Swagger
Swagger-generated API documentation is available at
```
http://localhost:9920/swagger-ui/#/
```