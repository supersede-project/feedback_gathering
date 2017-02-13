#!/bin/bash

echo -e "\nCleaning and building Monitor Manager...\n"
./gradlew clean build --refresh-dependencies

cd ../monitors/twitter
echo -e "\nCleaning and building Twitter Monitor..\n"
./gradlew clean build --refresh-dependencies

cd ../appStore
echo -e "\nCleaning and building AppStore Monitor...\n"
./gradlew clean build --refresh-dependencies

cd ../googlePlay
echo -e "\nCleaning and building Google Play Monitor...\n"
./gradlew clean build --refresh-dependencies

cd ../MonitoringUserEvents/PrjMonitoringUserEvents
echo -e "\nCleaning and building MonitoringUserEvents Monitor...\n"
mvn clean package 

