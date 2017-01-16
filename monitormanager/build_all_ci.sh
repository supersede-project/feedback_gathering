#!/bin/bash

echo -e "\nCleaning and building Monitor Manager...\n"
./gradlew clean build 

cd ../monitors/twitter
echo -e "\nCleaning and building Twitter Monitor..\n"
./gradlew clean build 

cd ../appStore
echo -e "\nCleaning and building AppStore Monitor...\n"
./gradlew clean build

cd ../googlePlay
echo -e "\nCleaning and building Google Play Monitor...\n"
./gradlew clean build
