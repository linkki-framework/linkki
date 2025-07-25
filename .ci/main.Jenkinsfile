library 'f10-jenkins-library@1.1_patches'
library 'druiden-jenkins-library@main'

pipeline {
    agent any

    environment {
        PROJECT_NAME = 'linkki'
        BRANCH_NAME = "${env.GIT_LOCAL_BRANCH}"
        PROJECT_ID = "${PROJECT_NAME}-${BRANCH_NAME.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        NETWORK_NAME = "network-${PROJECT_ID}"
        CONTAINER_RETENTION = 'permanent-on'
        DEPLOYMENT_NAME = "linkki-sample-test-playground-vaadin-flow"
        DEPLOYMENT_HOST = "${PROJECT_ID}.dockerhost.i.faktorzehn.de"
        DEPLOYMENT_URL = "http://${DEPLOYMENT_HOST}/${DEPLOYMENT_NAME}"
        BASE_IMAGE = 'spring:25.7'
        SUITE_VERSION = '26.1'
    }

    stages {
        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}.${env.BRANCH_NAME}"
                }
            }
        }

        stage('Build') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -U -T 6 \
                        -pl "!vaadin-flow/doc" \
                        -Pproduction \
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

        stage('Upload Documentation') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -U -T 6 \
                        -pl "vaadin-flow/doc" \
                        deploy \
                        -Ddoc.user=doc'
                }
            }
        }

        // wait for the sample applications to be ready to run the tests
        stage('Wait for Server') {
            steps {
                waitForServer "${DEPLOYMENT_URL}"
            }
        }

        stage('Tests') {
            // Run Tests only if build was successful
            when {
                expression {
                    return currentBuild.currentResult == "SUCCESS"
                }
            }
            parallel {
                stage('UI Test') {
                    steps {
                        // check handles on chrome BEVOR ui tests
                        script {
                            def status = sh(returnStatus: true, script: 'pgrep chrome')
                            if (status == 0) {
                                def ret = sh(returnStdout: true, script: 'pgrep chrome')
                                echo "Es sind ${ret} handles auf Chrome vorhanden"
                            } else if(status == 1) {
                                echo "Keine handles auf Chrome vorhanden"
                            } else {
                                echo "Fehler beim Aufruf!"
                            }
                        }

                        withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                            sh 'mvn -f vaadin-flow/samples/test-playground/uitest/pom.xml test \
                                -Dmaven.test.failure.ignore=true -Dsurefire.rerunFailingTestsCount=3'
                        }

                        // check handles on chrome AFTER ui tests
                        script {
                            def status = sh(returnStatus: true, script: 'pgrep chrome')
                            if (status == 0) {
                                def ret = sh(returnStdout: true, script: 'pgrep chrome')
                                echo "Es sind ${ret} handles auf Chrome vorhanden"
                            } else if(status == 1) {
                                echo "Keine handles auf Chrome vorhanden"
                            } else {
                                echo "Fehler beim Aufruf!"
                            }
                        }

                        archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                        junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports-*/*.xml'
                    }

                    environment {
                        MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME}'
                    }
                }

                // run the SonarQube analysis for this change
                stage('SonarQube Analysis') {
                    steps {
                        withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21') {
                            runSonarQubeAnalysis()
                        }
                    }
                }

                stage('Dependency-Check') {
                    steps {
                        withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                            dependencyCheck version: "${SUITE_VERSION}"
                        }
                        script {
                            cveOutput("${SUITE_VERSION}")
                        }
                    }
                }
            }
        }

        stage('External Link Tests') {
            steps {
                retry(3) {
                    withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 17', publisherStrategy: 'EXPLICIT') {
                        sh 'mvn -U -pl "vaadin-flow/doc" jade:test-external-links'
                    }
                }
            }
        }

        stage('Check Git Diffs') {
            steps {
                verifyNoChangedFiles()
            }
        }

        // check if the SonarQube quality gate is fulfilled
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            unstable("SonarQube failed with status: ${qg.status}")
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            recordIssues enabledForFailure: true, qualityGates: [[threshold: 1, type: 'NEW', unstable: true]], tools: [java(), javaDoc(), spotBugs(), checkStyle(), sonarQube()]
            jacoco sourceInclusionPattern: '**/*.java'

            archiveArtifacts 'vaadin-flow/doc/target/doc/**'
        }

        regression {
            sendFailureEmail()
        }
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
    }

}