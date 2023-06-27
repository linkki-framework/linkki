#!/bin/bash
BUILD_NAME=$1

# Create user-defined bridge
NETWORK_NAME="network-linkki-$BUILD_NAME"
if [ -z "$(docker network ls --filter="name=^$NETWORK_NAME$" -q)" ]; then
    docker network create $NETWORK_NAME
fi

# Create container
CONTAINER_NAME="linkki-$BUILD_NAME"
if [ -n "$(docker container ls --filter="name=^$CONTAINER_NAME$" -a -q)" ]; then
    docker rm --force $CONTAINER_NAME
fi

docker create \
        --cpus=2 --memory=1g \
        --name $CONTAINER_NAME \
        --network $NETWORK_NAME \
        --label "url=$CONTAINER_NAME" \
        --label "entry-path=linkki-sample-test-playground-vaadin23" \
        --label "retention=${CONTAINER_RETENTION:-discard}" \
        harbor.faktorzehn.de/suite-base/spring:22.12

# Copy WAR to container
WAR_FILE="vaadin23/samples/test-playground/target/linkki-sample-test-playground-vaadin23.war"
# Container path must use application.jar in order to work with the harbor image.
# This still works even if a WAR is copied.
docker cp $WAR_FILE $CONTAINER_NAME:/opt/spring/application.jar

# Start the container
docker start $CONTAINER_NAME
