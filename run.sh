#!/bin/sh
set -e

./mvnw clean package
java -jar target/carservice-0.0.1-SNAPSHOT.jar