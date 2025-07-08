library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    environment {
        PROJECT_NAME = 'linkki'
        BUILD_NAME = 'next-JavaLTS'
        PROJECT_ID = "${PROJECT_NAME}-${BUILD_NAME.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        NETWORK_NAME = "network-${PROJECT_ID}"
        CONTAINER_RETENTION = 'keep'
        DEPLOYMENT_NAME = "linkki-sample-test-playground-vaadin-flow"
        DEPLOYMENT_HOST = "${PROJECT_ID}.dockerhost.i.faktorzehn.de"
        DEPLOYMENT_URL = "http://${DEPLOYMENT_HOST}/${DEPLOYMENT_NAME}"
        BASE_IMAGE = 'spring:25.7'
    }

    stages {
        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}.${env.BUILD_NAME}"
                    currentBuild.description = "Runs and tests the application with the next Java LTS"
                }
            }
        }

        stage('Build') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -U -T 6 \
                            -P production \
                            -pl "!vaadin-flow/doc" \
                            -pl "!vaadin-flow/apt" \
                            clean source:jar javadoc:jar deploy'
                }
            }
        }

        stage('Dependency-Check') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    dependencyCheck version: '25.1'
                }
            }
        }

        stage('Remove Docker Network') {
            steps {
                deleteDockerNetwork "${NETWORK_NAME}"
            }
        }

        stage('Docker Deployment') {
            steps {

                sh 'cp vaadin-flow/samples/test-playground/target/linkki-sample-test-playground-vaadin-flow.war .ci/docker/playground/'
                sh 'cp vaadin-flow/samples/linkki-f10-sample/target/linkki-f10-sample.jar .ci/docker/linkki-f10-sample/'
                dir('.ci/docker') {
                    sh "docker compose -f docker-compose.yml -f docker-compose.resources.yml -p ${PROJECT_ID} up -d --build"
                }

                rtp parserName: 'HTML', nullAction: '1', stableText: """
                    <h3>Sample Deployments</h3>
                    <ul>
                        <li><a href='${DEPLOYMENT_URL}' target="_blank">Playground</a></li>
                        <li><a href='http://${PROJECT_ID}-linkki-f10-sample.dockerhost.i.faktorzehn.de/linkki-f10-sample/ui' target="_blank">Linkki F10 Sample</a></li>
                    </ul>
                """
            }
        }

        // wait for the sample applications to be ready to run the tests
        stage('Wait for Server') {
            steps {
                waitForServer "${DEPLOYMENT_URL}"
            }
        }

        stage('UI Test') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -f vaadin-flow/samples/test-playground/uitest/pom.xml test \
                        -Dmaven.test.failure.ignore=true -Dsurefire.rerunFailingTestsCount=3'
                }

                archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports-*/*.xml'
            }

            environment {
                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME}'
            }
        }

        stage('Check Git Diffs') {
            steps {
                verifyNoChangedFiles()
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            recordIssues enabledForFailure: true, qualityGates: [[threshold: 1, type: 'NEW', unstable: true]], tools: [java(), javaDoc(), spotBugs(), checkStyle()]
            jacoco sourceInclusionPattern: '**/*.java'
        }

        regression {
            sendFailureEmail()
        }
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

}