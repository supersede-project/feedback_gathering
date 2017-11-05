# Introduction

Is a RESTfull Web API that provides endpoints for storing, receiving and deleting user feedbacks.

# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Installation](#installation)
- [Deployment](#deployment)
- [Tests](#tests)
- [Docker](#docker)
- [License](#license)

# Installation

```bash
# clone the repository

cd repository

# copy the configuration files for your configuration
cp src/main/resources/application.properties-dist src/main/resources/application.properties
cp src/main/resources/application-test.properties-dist src/main/resources/application-test.properties
```

Create a test database and a local database to run the application locally if needed. You can find the newest DB dump in src/main/resources/db. Fill in your DB credentials and all other required values in the newly created application.properties and application-test.properties. Do not add this file to the GIT index.

```bash
cd repository
# create the gradle properties file and fill in the credentials (ask Yosu <jesus.gorronogoitia@atos.net> or Ronnie <ronnieschaniel@gmail.com>)
cp gradle.properties-dist gradle.properties 

# install the project's dependencies and generate the war file in build/libs/
gradle build
```

DB dump in src/main/resources/db/migrations/supersede_repository_db_structure.sql

# Tests

To run the integration tests, execute the following commands:

```bash
gradle test
```

# Deployment

Executing 'gradle build' in the root folder creates a build folder. In build/libs there is a war file. This war file can be deployed, e.g. on a Apache Tomcat. 


# IntelliJ

The suggested IDE is IntelliJ, but Eclipse works as well. 



# Docker

Build the JAR and the images and containers for the Java Spring Repository and the Repository DB. 

The jdbs connection string in the application.properties should have the DB container service name as host:

```bash
spring.datasource.url = jdbc:mysql://mysqldbserver:3306/<db_name>?useSSL=false
```

```bash
gradle clean build jar
docker-compose up -d
```

Check if the 2 containers are up and running:
 
```bash
docker ps -a  
```

## Troubleshooting

You might get: 
```bash
ERROR org.springframework.boot.SpringApplication - Application startup failed
...
Caused by: java.lang.IllegalArgumentException: No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.
```

Completely delete the container and image: 
```bash
docker rm -v <container_name>
docker rmi <image_name>
```

**Attention:** This would delete all stopped containers, all unused images and all dangling volumes:
```bash
docker ps -q |xargs docker rm
docker images -q |xargs docker rmi
docker volume rm `docker volume ls -q -f dangling=true`
```

Finally execute:
```bash
gradle build jar -x test
gradle bootRepackage
docker-compose up -d 
```


# License

Apache License 2.0
