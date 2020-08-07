#!/bin/bash
export POM_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)

if [[ "$POM_VERSION" =~ ^([0-9]+\.[0-9]+\.[0-9]+\-SNAPSHOT)$ ]]; then
  echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
  docker build -t memerson .
  docker images
  docker tag memerson $DOCKER_USERNAME/memerson:$POM_VERSION
  docker push $DOCKER_USERNAME/memerson:$POM_VERSION
fi

exit 1
