#!/bin/bash

# builds a jar with the dependencies in there
gradle bootRepackage
./gradlew clean build jar -x test

docker-compose up -d
