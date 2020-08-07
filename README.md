# financasapi
[![Build Status](https://travis-ci.org/emerson-matos/financasapi.svg?branch=master)](https://travis-ci.org/emerson-matos/financasapi)
[![codecov](https://codecov.io/gh/emerson-matos/financasapi/branch/master/graph/badge.svg)](https://codecov.io/gh/emerson-matos/financasapi)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=emerson-matos_financasapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=emerson-matos_financasapi)

## Summary
The objective of this project is to be able to study Spring Framework with something concrete building a backend to make financial control.

## Prerequisites
* [Maven](https://maven.apache.org/download.cgi)
* [Java JDK 11](https://java.com/en/download/help/download_options.xml)
* [Postgres](https://www.postgresql.org/download/)

## Running

### With [Docker](https://www.docker.com/):
```
docker-compose up -d
```
then just go to [localhost on port 8080](http://localhost:8080)

### Without [Docker](https://www.docker.com/):

You will need to set the following environment variables:
* FIREBASE_URL
> If you desire to use firebase auth
* SPRING_PROFILE
> To spring dev tools know the actual environment, just set to dev if you don't know how it works
* GOOGLE_CREDENTIALS
> If you want to use google firebase credentials like a string without a json file
* GOOGLE_APPLICATION_CREDENTIALS
> If you want to use google firebase credentials from a json file located somewhere on your machine
___
To turn firebase auth off just change the value of `br.com.controle.financeiro.firebase.enabled` to `false`

After that step we will need [postgres](https://www.postgresql.org/download/), [maven](https://maven.apache.org/download.cgi) and [Java JDK 11](https://java.com/en/download/help/download_options.xml)

You need to create a database and change on application.properties for your Spring Profile these properties:
* spring.datasource.username
** It will be your database username. Ex.: postgres
* spring.datasource.password
** It will be your database password. Ex.: postgres
* spring.datasource.url
** It will be your database url. Ex.: jdbc:postgresql://localhost:5432/controle

With all setup ready then its just go to the base folder and do as follows
```
mvn spring-boot:run
```

## References
* [Best Practices for a pragmatic restful post responses](https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api)
* [Project Skeleton for Spring Boot Web Services](https://github.com/leanstacks/skeleton-ws-spring-boot)
* [An Intro to Spring HATEOAS](https://www.baeldung.com/spring-hateoas-tutorial)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Github Open Source Guides](https://opensource.guide/)
* [Spring-Boot-Starter](https://github.com/savicprvoslav/Spring-Boot-starter)
* [Spring Boot Conditionals](https://reflectoring.io/spring-boot-conditionals/)
* [Spring-Boot-Starter (Using Firebase Auth)](https://github.com/savicprvoslav/Spring-Boot-starter/)
* [Add the Firebase Admin SDK to your server](https://firebase.google.com/docs/admin/setup)

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
