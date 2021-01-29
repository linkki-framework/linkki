#!/bin/bash
BUILD_NAME=$1
WILDFLY_NAME="wildfly-linkki-$BUILD_NAME"

# Vaadin 8
WAR_FILE="vaadin8/samples/binding/target/linkki-sample-binding-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-binding-vaadin8.war

WAR_FILE="vaadin8/samples/messages/target/linkki-sample-messages-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-messages-vaadin8.war

WAR_FILE="vaadin8/samples/custom-layout/target/linkki-sample-custom-layout-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-custom-layout-vaadin8.war

WAR_FILE="vaadin8/samples/tree-table/target/linkki-sample-tree-table-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-tree-table-vaadin8.war

WAR_FILE="vaadin8/samples/application-framework/target/linkki-sample-application-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-application-vaadin8.war

WAR_FILE="vaadin8/samples/getting-started/target/linkki-getting-started-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-getting-started-vaadin8.war

WAR_FILE="vaadin8/samples/ips/target/linkki-sample-ips-vaadin8.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-ips-vaadin8.war

# Vaadin 14
WAR_FILE="vaadin14/samples/messages/target/linkki-sample-messages-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-messages-vaadin14.war

WAR_FILE="vaadin14/samples/application-framework/target/linkki-sample-application-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-application-vaadin14.war

WAR_FILE="vaadin14/samples/getting-started/target/linkki-getting-started-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-getting-started-vaadin14.war

WAR_FILE="vaadin14/samples/tree-table/target/linkki-sample-tree-table-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-tree-table-vaadin14.war

WAR_FILE="vaadin14/samples/binding/target/linkki-sample-binding-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-binding-vaadin14.war

WAR_FILE="vaadin14/samples/ips/target/linkki-sample-ips-vaadin14.war"
docker cp $WAR_FILE $WILDFLY_NAME:/opt/jboss/wildfly/standalone/deployments/linkki-sample-ips-vaadin14.war