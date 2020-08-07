#!/bin/bash
echo "INICIANDO SNAPSHOT.SH"
if [[ "$POM_VERSION" =~ ^([0-9]+\.[0-9]+\.[0-9]+\-SNAPSHOT)$ ]]; then
  echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
  echo "DOCKER BUILD"
  docker build -t memerson .
  echo "DOCKER TAG"
  docker tag memerson $DOCKER_USERNAME/memerson:$SNAPSHOT_VERSION
  echo "DOCKER PUSH"
  docker push $DOCKER_USERNAME/memerson:$SNAPSHOT_VERSION
fi
echo "FIM SNAPSHOT.SH"
exit 0
