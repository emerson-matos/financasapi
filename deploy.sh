#!/bin/bash

echo "INICIANDO DEPLOY.SH"
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
echo "DOCKER BUILD"
docker build -t memerson .
echo "DOCKER TAG"
docker tag memerson $DOCKER_USERNAME/memerson:latest
docker tag memerson $DOCKER_USERNAME/memerson:$POM_VERSION
echo "DOCKER PUSH"
docker push $DOCKER_USERNAME/memerson:latest
docker push $DOCKER_USERNAME/memerson:$POM_VERSION
echo "MVN DEPLOY"
mvn deploy -s .travis.settings.xml -DskipTests

echo "FIM DEPLOY.SH"
exit 0
