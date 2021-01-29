#!/bin/bash
BUILD_NAME=$1

# Create user-defined bridge
NETWORK_NAME="network-linkki-$BUILD_NAME"
if [ -z "$(docker network ls --filter="name=$NETWORK_NAME" -q)" ]; then
	docker network create $NETWORK_NAME
fi

# Create wildfly container
WILDFLY_NAME="wildfly-linkki-$BUILD_NAME"
if [ -n "$(docker container ls --filter="name=$WILDFLY_NAME" -a -q)" ]; then
	docker rm --force $WILDFLY_NAME
fi
LABEL="url=linkki-$BUILD_NAME"
docker create --cpus=2 --memory=4g --name $WILDFLY_NAME --network $NETWORK_NAME --label $LABEL --label "retention=${CONTAINER_RETENTION:-discard}" f10/wildfly-linkki:8

# Vaadin 8
WAR_FILE="vaadin8/samples/test-playground/target/linkki-sample-test-playground-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-test-playground-vaadin8.war

WAR_FILE="vaadin8/samples/dynamic-fields/target/linkki-sample-dynamic-fields-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-dynamic-fields-vaadin8.war

# Vaadin 14
WAR_FILE="vaadin14/samples/test-playground/target/linkki-sample-test-playground-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-test-playground-vaadin14.war

WAR_FILE="vaadin14/samples/dynamic-fields/target/linkki-sample-dynamic-fields-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-dynamic-fields-vaadin14.war

docker start $WILDFLY_NAME