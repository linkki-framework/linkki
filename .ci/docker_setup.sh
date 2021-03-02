#!/bin/bash
BUILD_NAME=$1

# Create user-defined bridge
NETWORK_NAME="network-linkki-$BUILD_NAME"
if [ -z "$(docker network ls --filter="name=$NETWORK_NAME" -q)" ]; then
	docker network create $NETWORK_NAME
fi

# Create spring boot container for playground
SPRINGBOOT_NAME="linkki-$BUILD_NAME"
if [ -n "$(docker container ls --filter="name=$SPRINGBOOT_NAME" -a -q)" ]; then
    docker rm --force $SPRINGBOOT_NAME
fi

LABEL="url=linkki-$BUILD_NAME"
docker create \
        --cpus=2 --memory=4g \
        --name $SPRINGBOOT_NAME \
        --network $NETWORK_NAME \
        --label $LABEL \
        --label "retention=${CONTAINER_RETENTION:-discard}" \
        f10/spring:8

# Copy war to container
WAR_FILE="vaadin8/samples/test-playground/target/linkki-sample-test-playground-vaadin8.war"
docker cp $WAR_FILE $SPRINGBOOT_NAME:/opt/spring/application.war

# Start the container
docker start $SPRINGBOOT_NAME
