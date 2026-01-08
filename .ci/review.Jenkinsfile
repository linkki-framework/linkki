library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    environment {
        PROJECT_NAME = 'linkki'
        BRANCH_NAME = "${env.GERRIT_BRANCH}"
        BUILD_NAME = "${env.GERRIT_CHANGE_NUMBER}"
        PROJECT_ID = "${PROJECT_NAME}-${BUILD_NAME.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        NETWORK_NAME = "network-${PROJECT_ID}"
        MAVEN_REPOSITORY = "${env.WORKSPACE}/.repository"
        DEPLOYMENT_NAME = "linkki-sample-test-playground-vaadin-flow"
        DEPLOYMENT_HOST = "${PROJECT_ID}.dockerhost.i.faktorzehn.de"
        DEPLOYMENT_URL = "http://${DEPLOYMENT_HOST}/${DEPLOYMENT_NAME}"
        BASE_IMAGE = 'spring:25.1'
    }

    tools {
        jdk 'OpenJDK 17'
        maven 'maven 3.9'
    }

    stages {

        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}.${env.BRANCH_NAME}.${env.BUILD_NAME}"
                }

                preBuildSteps()

                withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT') {
                    dependsOn(credentials: 'gerrit_rest') {}
                }
            }
        }

        stage('Fast Build') {
            steps {
                withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                    sh 'mvn -U -T 6 \
                        clean install \
                        -Pproduction \
                        -Dmaven.test.skip \
                        -Drevapi.skip \
                        -Dspotbugs.skip=true \
                        -Dcheckstyle.skip=true \
                        -Djacoco.skip=true \
                        -Denforcer.skip=true \
                        -Dmaven.javadoc.skip=true \
                        -Djade.skip=true \
                        -Dflatten.skip \
                        '
                }
            }
        }

        stage('Build and Checks') {
            parallel {
                stage('Check API') {
                    steps {
                        catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                            withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT') {
                                sh 'mvn -T 6 \
                                    -pl "!linkki-codeanalysis" \
                                    validate org.revapi:revapi-maven-plugin:check'
                            }
                        }
                    }
                }

                stage('Build & Analysis') {
                    stages {
                        stage('Full Build') {
                            steps {
                                withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                                    sh 'mvn -U -T 6 \
                                        install javadoc:javadoc  \
                                        -Drevapi.skip \
                                        -Dflatten.skip \
                                        -Dmaven.test.failure.ignore=true \
                                        -Pproduction'
                                }

                                archiveArtifacts 'vaadin-flow/doc/target/doc/**'
                            }
                        }
                        stage('Analysis') {
                            steps {
                                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 17') {
                                    runSonarQubeAnalysis()
                                }
                            }
                        }
                        stage('Quality Gate') {
                            steps {
                                timeout(time: 1, unit: 'HOURS') {
                                    script {
                                        def qg = waitForQualityGate()
                                        if (qg.status != 'OK') {
                                            unstable("SonarQube failed with status: ${qg.status}")
                                        }
                                        echo "Sonar Qube: https://sonarqube.faktorzehn.de/dashboard?id=${env.PROJECT_NAME}&pullRequest=${env.GERRIT_CHANGE_NUMBER}"
                                    }
                                }
                            }
                        }
                    }
                }

                stage('Faktor-IPS Build') {
                    steps {
                        withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT') {
                            sh 'mvn \
                                -pl org.linkki-framework:linkki-ips-vaadin-flow \
                                -Dmaven.repo.local="${MAVEN_REPOSITORY}" \
                                faktorips:faktorips-clean faktorips:faktorips-build'
                        }
                    }
                }

                stage('Deployment and UI Test') {
                    stages {
                        stage('Remove Docker Network') {
                            steps {
                                deleteDockerNetwork "${NETWORK_NAME}"
                            }
                        }

                        stage('Docker Deployment') {
                            steps {
                                sh 'bash -x .ci/docker_setup.sh ${PROJECT_ID} ${BASE_IMAGE}'

                                rtp parserName: 'HTML', nullAction: '1', stableText: """
                                    <h3>Sample Deployments</h3>
                                    <ul>
                                        <li><a href='${DEPLOYMENT_URL}' target="_blank">Playground</a></li>
                                    </ul>
                                """
                            }
                        }

                        stage('Wait for Server') {
                            steps {
                                waitForServer "${DEPLOYMENT_URL}"
                            }
                        }

                        stage('UI Test') {
                            steps {
                                // check handles on chrome BEFORE ui tests
                                script {
                                    def status = sh(returnStatus: true, script: 'pgrep chrome')
                                    if (status == 0) {
                                        def ret = sh(returnStdout: true, script: 'pgrep chrome')
                                        echo "There are ${ret} handles on chrome"
                                    } else if(status == 1) {
                                        echo "No handles on chrome"
                                    } else {
                                        echo "Error retrieving handles on chrome"
                                    }
                                }

                                withMaven(mavenLocalRepo: '${MAVEN_REPOSITORY}', publisherStrategy: 'EXPLICIT') {
                                    sh 'mvn \
                                        -f vaadin-flow/samples/test-playground/uitest/pom.xml \
                                        test \
                                        -Dmaven.test.failure.ignore=true \
                                        -Dsurefire.rerunFailingTestsCount=3 \
                                        -T 6'
                                }

                                // check handles on chrome AFTER ui tests
                                script {
                                    def status = sh(returnStatus: true, script: 'pgrep chrome')
                                    if (status == 0) {
                                        def ret = sh(returnStdout: true, script: 'pgrep chrome')
                                        echo "There are ${ret} handles on chrome"
                                    } else if(status == 1) {
                                        echo "No handles on chrome"
                                    } else {
                                        echo "Error retrieving handles on chrome"
                                    }
                                }

                                archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                                junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports-*/*.xml'
                            }

                            environment {
                                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME}'
                            }
                        }
                    }
                }
            }
        }

        stage('Check Git Diffs') {
            steps {
                verifyNoChangedFiles()
            }
        }

        stage('Collect Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
                recordIssues enabledForFailure: true, qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]], tools: [java(), javaDoc(), spotBugs(), checkStyle()]
                jacoco sourceInclusionPattern: '**/*.java'
            }
        }
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
    }

}