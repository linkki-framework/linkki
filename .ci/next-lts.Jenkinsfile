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
        BASE_IMAGE = 'spring:26.1-java25'
        SUITE_VERSION = '26.1'
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
                            -pl "!vaadin-flow/doc" \
                            -pl "!vaadin-flow/apt" \
                            clean source:jar javadoc:jar deploy'
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
                script {
                    env.DEPLOYMENT_URL = getContainerUrl(PROJECT_ID, DEPLOYMENT_NAME)
                    env.DEPLOYMENT_HOST = env.DEPLOYMENT_URL.replaceAll('https?://([^/]+).*', '$1')
                    env.F10_DEPLOYMENT_URL = getContainerUrl("${PROJECT_ID}-linkki-f10-sample", 'linkki-f10-sample/ui')
                    env.F10_DEPLOYMENT_HOST = env.F10_DEPLOYMENT_URL.replaceAll('https?://([^/]+).*', '$1')
                }

                sh 'cp vaadin-flow/samples/test-playground/target/linkki-sample-test-playground-vaadin-flow.war .ci/docker/playground/'
                sh 'cp vaadin-flow/samples/linkki-f10-sample/target/linkki-f10-sample.jar .ci/docker/linkki-f10-sample/'
                dir('.ci/docker') {
                    sh "docker compose -f docker-compose.yml -f docker-compose.resources.yml -p ${PROJECT_ID} up -d --build"
                }

                rtp parserName: 'HTML', nullAction: '1', stableText: """
                    <h3>Sample Deployments</h3>
                    <ul>
                        <li><a href='${env.DEPLOYMENT_URL}' target="_blank">Playground</a></li>
                        <li><a href='${env.F10_DEPLOYMENT_URL}' target="_blank">Linkki F10 Sample</a></li>
                    </ul>
                """
            }
        }

        // wait for the sample applications to be ready to run the tests
        stage('Wait for Server') {
            steps {
                waitForServer env.DEPLOYMENT_URL
                waitForServer env.F10_DEPLOYMENT_URL
            }
        }

        stage('UI Test') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn \
                        -f vaadin-flow/samples/test-playground/uitest/pom.xml \
                        test \
                        -Dmaven.test.failure.ignore=true \
                        -Dsurefire.rerunFailingTestsCount=3 \
                        -Pheadless'
                }

                script {
                    archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                    def result = junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports/*.xml'

                    if (result.getFailCount() > 0) {
                        rtp parserName: 'HTML', nullAction: '1', stableText: """
                            <h3>UI Test</h3>
                            <ul>
                                <li><a href='${env.BUILD_URL}artifact/vaadin-flow/samples/test-playground/uitest/target/error-screenshots/' target="_blank">Error Screenshots</a></li>
                                <li><a href='https://jira.convista.com/issues/?jql=project%20%3D%20LIN%20AND%20labels%20%3D%20UI-Tests' target="_blank">Jira Issues</a></li>
                                <li>Falls es noch kein Jira Issue gibt:
                                    <ul>
                                        <li><a href='https://jira.convista.com/secure/CreateIssueDetails!init.jspa?pid=15370&issuetype=1&priority=6&components=16576&labels=UI-Tests&summary=UI-TestXXX&description=${env.BUILD_URL}' target="_blank">Issue anlegen</a></li>
                                        <li>In der Test Methode die Beschreibung der Assertion um die Issuenummer ergänzen</li>
                                    </ul>
                                </li>
                            </ul>
                        """
                    }
                }
            }

            environment {
                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME} -Dtest.hostname.f10=${F10_DEPLOYMENT_HOST} -Dtest.port.f10=80'
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