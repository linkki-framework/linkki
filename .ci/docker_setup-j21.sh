#!/bin/bash
BUILD_NAME=$1

#######################################################
# extra script to be used in nightly to run the
# playground within an image with JDK 21
#######################################################

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
        --label "entry-path=linkki-sample-test-playground-vaadin-flow" \
        --label "retention=${CONTAINER_RETENTION:-discard}" \
        -e "JAVA_TOOL_OPTIONS=-Xms250m -Xmx750m" \
        harbor.faktorzehn.de/suite-base/spring:24.7-java21

# Copy war to container
WAR_FILE="vaadin-flow/samples/test-playground/target/linkki-sample-test-playground-vaadin-flow.war"
docker cp $WAR_FILE $CONTAINER_NAME:/opt/spring/application.jar

# Start the container
docker start $CONTAINER_NAME
