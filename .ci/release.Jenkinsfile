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
        PROJECT_ID = "${PROJECT_NAME}-${params.RELEASE_VERSION.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        CONTAINER_RETENTION = 'keep'
        BASE_IMAGE = 'spring:25.7'
        SUITE_VERSION = '26.1'
    }

    stages {
        stage('Prepare Release') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    script {
                        prepareRelease additionalModules: ['vaadin-flow/samples/test-playground/uitest']
                    }
                }
            }
        }

        stage('Build') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    sh 'mvn -U -P production clean javadoc:jar source:jar install'
                }

                junit "**/target/surefire-reports/*.xml"
                recordIssues enabledForFailure: true, qualityGates: [[threshold: 1, type: 'NEW', unstable: true]], tools: [java(), javaDoc(), spotBugs(), checkStyle()]
                jacoco sourceInclusionPattern: '**/*.java'
            }
        }

        stage('Dependency-Check') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    dependencyCheck version: "${SUITE_VERSION}"
                }
            }
        }

        stage('Check Git Diffs') {
            steps {
                verifyNoChangedFiles()
            }
        }

        stage('Docker Deployment') {
            steps {
                sh 'bash -x .ci/docker_setup.sh ${PROJECT_ID} ${BASE_IMAGE}'

                rtp parserName: 'HTML', nullAction: '1', stableText: """
                    <h3>Sample Deployments</h3>
                    <ul>
                        <li><a href='http://${env:PROJECT_ID}.dockerhost.i.faktorzehn.de/linkki-sample-test-playground-vaadin-flow'>Playground</a></li>
                    </ul>
                """
            }
        }

        // wait for the sample applications to be ready to run the tests
        stage('Wait for Server') {
            steps {
                waitForServer "http://${PROJECT_ID}.dockerhost.i.faktorzehn.de/linkki-sample-test-playground-vaadin-flow"
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
                    sh 'mvn -f vaadin-flow/samples/test-playground/uitest/pom.xml test \
                        -Dmaven.test.failure.ignore=true -Dsurefire.rerunFailingTestsCount=3'
                }

                archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports-*/*.xml'
            }

            environment {
                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${PROJECT_ID}.dockerhost.i.faktorzehn.de -Dtest.port=80 -Dtest.path=linkki-sample-test-playground-vaadin-flow'
            }
        }

        stage('Upload Release') {
            steps {
                script {
                    uploadRelease() {
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