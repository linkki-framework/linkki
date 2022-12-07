#!/bin/bash

BUILD_NAME=$1

# Create user-defined bridge
NETWORK_NAME="network-linkki-$BUILD_NAME"
if [ -z "$(docker network ls --filter="name=^$NETWORK_NAME$" -q)" ]; then
    docker network create $NETWORK_NAME
fi

# Create vaadin14 container
V14_NAME="linkki-vaadin14-$BUILD_NAME"
if [ -n "$(docker container ls --filter="name=$V14_NAME" -a -q)" ]; then
    docker rm --force $V14_NAME
fi

docker create \
        --cpus=2 --memory=4g \
        --name $V14_NAME \
        --network $NETWORK_NAME \
        --label "url=$V14_NAME" \
        --label "entry-path=linkki-sample-test-playground-vaadin14" \
        --label "retention=${CONTAINER_RETENTION:-discard}" \
        f10/spring:8

# Copy war to container
WAR_FILE="vaadin14/samples/test-playground/target/linkki-sample-test-playground-vaadin14.war"
docker cp $WAR_FILE $V14_NAME:/opt/spring/application.war

# Start the container
docker start $V14_NAME

# Create vaadin8 container
V8_NAME="linkki-vaadin8-$BUILD_NAME"
if [ -n "$(docker container ls --filter="name=$V8_NAME" -a -q)" ]; then
    docker rm --force $V8_NAME
fi

docker create \
        --cpus=2 --memory=4g \
        --name $V8_NAME \
        --network $NETWORK_NAME \
        --label "url=$V8_NAME" \
        --label "entry-path=linkki-sample-test-playground-vaadin8" \
        --label "retention=${CONTAINER_RETENTION:-discard}" \
        f10/spring:8

# Copy war to container
WAR_FILE="vaadin8/samples/test-playground/target/linkki-sample-test-playground-vaadin8.war"
docker cp $WAR_FILE $V8_NAME:/opt/spring/application.war

# Start the container
docker start $V8_NAME
