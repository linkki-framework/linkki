library 'f10-jenkins-library@1.1_patches'
library 'release-library@1.2'
library 'maven-central-release-library@main'

import groovy.json.JsonOutput

pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'
        maven 'maven 3.9'
    }

    environment {
        PROJECT_NAME = 'linkki'
        NETWORK_NAME = "network-${PROJECT_ID}"
        PROJECT_ID = "${PROJECT_NAME}-${params.RELEASE_VERSION.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        DEPLOYMENT_NAME = "linkki-sample-test-playground-vaadin-flow"
        DEPLOYMENT_HOST = "${PROJECT_ID}.dockerhost.i.faktorzehn.de"
        DEPLOYMENT_URL = "http://${DEPLOYMENT_HOST}/${DEPLOYMENT_NAME}"
        F10_DEPLOYMENT_HOST = "${PROJECT_ID}-linkki-f10-sample.dockerhost.i.faktorzehn.de"
        F10_DEPLOYMENT_URL = "http://${F10_DEPLOYMENT_HOST}/linkki-f10-sample/ui"
        BASE_IMAGE = 'spring:26.1'
        SUITE_VERSION = '26.1'
    }

    stages {
        stage('Prepare Release') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    prepareRelease additionalModules: ['vaadin-flow/samples/test-playground/uitest']
                }
            }
        }

        stage('Build') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -U clean javadoc:jar source:jar install'
                }

                junit "**/target/surefire-reports/*.xml"
                recordIssues enabledForFailure: true, qualityGates: [[threshold: 1, type: 'NEW', unstable: true]], tools: [java(), javaDoc(), spotBugs(), checkStyle()]
                jacoco sourceInclusionPattern: '**/*.java'
            }
        }

        stage('Remove Docker Network') {
            steps {
                deleteDockerNetwork "${NETWORK_NAME}"
            }
        }

        stage('Check Git Diffs') {
            steps {
                verifyNoChangedFiles()
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
            // Run Tests only if build was successful
            when {
                expression {
                    return currentBuild.currentResult == "SUCCESS"
                }
            }

            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    sh 'mvn \
                        -f vaadin-flow/samples/test-playground/uitest/pom.xml \
                        test \
                        -Dmaven.test.failure.ignore=true \
                        -Dsurefire.rerunFailingTestsCount=3 \
                        -Pheadless'
                }

                archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports-*/*.xml'
            }

            environment {
                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME} -Dtest.hostname.f10=${F10_DEPLOYMENT_HOST} -Dtest.port.f10=80'
            }
        }

        stage('Upload Release') {
            steps {
                script {
                    uploadRelease() {
                        uploadDocumentation project: 'linkki', folder: 'vaadin-flow/doc/target/doc/html', legacyMode: false

                        // only publish final releases (no rcs & milestones) on maven central
                        if(params.RELEASE_VERSION ==~ /(\d+\.)+\d+/) {
                            deployToMavenCentral(releaseJobURL: "https://druiden-ci.faktorzehn.dev/view/linkki/job/linkki_Release_MavenCentral_Publish/build",
                                                 dropJobURL: "https://druiden-ci.faktorzehn.dev/view/linkki/job/linkki_Release_MavenCentral_Drop/build",
                                                 commands: ['mvn javadoc:jar source:jar deploy -PMavenCentralRelease -Ddoc.user=doc -Dmaven.test.skip=true'])
                        } else {
                            sh 'mvn javadoc:jar source:jar deploy -Ddoc.user=doc'
                        }
                    }
                }
            }
        }
    }

    post {
        unsuccessful {
            sendFailureEmail()
        }
    }

    options {
        skipDefaultCheckout true
        timeout(time: 1, unit: 'HOURS')
    }

}