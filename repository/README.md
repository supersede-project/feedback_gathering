# Introduction

Is a RESTfull Web API that provides endpoints for storing, receiving and deleting user feedbacks.

# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Installation](#installation)
- [Deployment](#deployment)
- [Tests](#tests)
- [Upload Directory Profile Picture](#upload-directory-profile-picture)
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

Create a test database and a local database to run 
the application locally if needed. You can find the 
newest DB dump in `src/main/resources/db/f2f_repository`.
 The dumps include SQL schemas for the (1) production db,
 (2) local db and (3) test db.
Fill in your DB credentials and all other required 
values in the newly created application.properties 
and application-test.properties. 
Do not add this file to the GIT index.
To configure the email newsletter, please refer to a detailed
description in the corresponding [WIKI page](https://github.com/supersede-project/monitor_feedback/wiki/F2F-Email-Newsletter-Serice).

```bash
cd repository
# create the gradle properties file and fill in the credentials (ask Yosu <jesus.gorronogoitia@atos.net> or Ronnie <ronnieschaniel@gmail.com>)
cp gradle.properties-dist gradle.properties 

# install the project's dependencies and generate the war file in build/libs/
gradle build
```

DB dump in `src/main/resources/db/migrations/supersede_repository_db_structure.sql`.
In order to update the db schema to incorporate the
additional feedback-to-feedback data models, apply
the change script located in `rc/main/resources/db/migrations/migration_supersede_f2f_repository.sql`.

# Tests

To run the integration tests, execute the following commands:

```bash
gradle test
```
Make sure to create a separate test db and apply
the script located in `src/main/resources/db/f2f_repository/test_monitor_feedback_repository_15_02_18.sql`.
# Deployment

Executing `gradle build` in the root folder creates a build folder. In `build/libs` there is a war file. This war file can be deployed, e.g. on a Apache Tomcat. 


# IntelliJ

The suggested IDE is IntelliJ, but Eclipse works as well. 

The prerequisites for running the application locally are a local Tomcat installation (just download the zip and unzip) and the Tomcat Server Plugin in IntelliJ. Then you can setup a run configuration for a local Tomcat: 

Select new local configuration:
![Run configuration](https://github.com/supersede-project/monitor_feedback/raw/develop_ronnie/images/tomcat_configuration.png)

Create the run configuration:

![Run configuration](https://raw.githubusercontent.com/supersede-project/monitor_feedback/develop_ronnie/images/run_configuation.png)

Add the war from `build/libs` as the artefact:

![Run configuration](https://github.com/supersede-project/monitor_feedback/raw/develop_ronnie/images/tomcat_configuration_artefact.png)

# Upload Directory Profile Picture
The newly integrated backward-feedback mechanisms
allow company representatives (e.g, developers) to
upload a profile picture (.png). This facilitates
end-users to recognize actors with admin privileges.
The upload directory on the production server as well
as local file system can be specified in the
`application.properties` respectively `application-test.properties`
files unter the key
```bash
supersede.profile_pic_upload_directory=/home/path/to/profile_pictures
```

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
