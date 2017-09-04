#!/bin/bash

# Image for service mysqldbserver was built because it did not already exist.
# To rebuild this image you must use `docker-compose build` or `docker-compose up --build`


# builds a jar with the dependencies in there
# gradle bootRepackage
# ./gradlew clean build


# docker-compose up -d --no-deps --build <service_name>
# --no-deps - Don't start linked services.
# --build - Build images before starting containers.

docker-compose up -d


# debug: docker exec -it orchestrator_springappserver_1 /bin/sh
