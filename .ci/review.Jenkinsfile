library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    environment {
        PROJECT_NAME = 'linkki'
        BRANCH_NAME = "${env.GERRIT_BRANCH}"
        BUILD_NAME = "${env.GERRIT_CHANGE_NUMBER}"
        PROJECT_ID = "${PROJECT_NAME}-${BUILD_NAME.replaceAll(/[^A-Za-z0-9]/, '-').toLowerCase()}"
        NETWORK_NAME = "network-${PROJECT_ID}"
        DEPLOYMENT_NAME = "linkki-sample-test-playground-vaadin-flow"
        DEPLOYMENT_HOST = "${PROJECT_ID}.dockerhost.i.faktorzehn.de"
        DEPLOYMENT_URL = "http://${DEPLOYMENT_HOST}/${DEPLOYMENT_NAME}"
        F10_DEPLOYMENT_HOST = "${PROJECT_ID}-linkki-f10-sample.dockerhost.i.faktorzehn.de"
        F10_DEPLOYMENT_URL = "http://${F10_DEPLOYMENT_HOST}/linkki-f10-sample/ui"
        BASE_IMAGE = 'spring:26.1'
    }

    tools {
        jdk 'OpenJDK 21'
        maven 'maven 3.9'
    }

    stages {

        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}.${env.BRANCH_NAME}.${env.BUILD_NAME}"
                }

                sh '''
                    echo "=== Node: $(hostname) ==="
                    echo "--- CPU ---"
                    nproc && grep "model name" /proc/cpuinfo | head -1
                    echo "--- Load ---"
                    uptime
                    echo "--- Memory ---"
                    free -h
                    echo "--- Disk I/O ---"
                    iostat -x 1 1 2>/dev/null | grep -v "^$" || echo "N/A"
                    echo "--- Disk ---"
                    df -h .
                    echo "--- NFS mounts ---"
                    mount | grep nfs || echo "none"
                    echo "--- Workspace filesystem type ---"
                    stat --file-system . 2>/dev/null || echo "N/A"
                    echo "--- Top processes by CPU ---"
                    ps aux --sort=-%cpu | head -10
                    echo "--- Top processes by MEM ---"
                    ps aux --sort=-%mem | head -10
                    echo "--- Java processes ---"
                    ps aux | grep java | grep -v grep || echo "none"
                    echo "--- Chrome processes ---"
                    ps aux | grep chrome | grep -v grep || echo "none"
                '''

                preBuildSteps()

                withMaven(publisherStrategy: 'EXPLICIT') {
                    dependsOn(credentials: 'gerrit_rest') {}
                }
            }
        }

        stage('Prepare for Deployment') {
            parallel {
                stage('Fast Build') {
                    steps {
                        withMaven(publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                            // Excluded modules:
                            //   doc: slow jade generation, not needed for downstream stages
                            //   linkki-codeanalysis: only needed as plugin dependency for checkstyle/spotbugs, which are unbound in skip-checks
                            // skip-frontend: Vaadin plugin is not thread-safe; skipping allows all modules to build in parallel
                            sh 'mvn -T 4 install -Pskip-checks,skip-frontend -pl "!vaadin-flow/doc,!linkki-codeanalysis" -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss'
                        }
                    }
                }

                stage('Remove Docker Network') {
                    steps {
                        deleteDockerNetwork "${NETWORK_NAME}"
                    }
                }
            }
        }

        stage('Build and Checks') {
            parallel {
                stage('Check API and Javadoc') {
                    steps {
                        catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                            withMaven(publisherStrategy: 'EXPLICIT') {
                                sh 'mvn -T 4 \
                                    -pl "!linkki-codeanalysis" \
                                    validate org.revapi:revapi-maven-plugin:check javadoc:javadoc'
                            }
                        }
                    }
                }

                stage('Build') {
                    stages {
                        stage('Full Build') {
                            steps {
                                withMaven(publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                                    sh 'mvn -T 8 \
                                        install \
                                        -pl "!vaadin-flow/doc" \
                                        -Drevapi.skip \
                                        -Dflatten.skip \
                                        -Dmaven.test.failure.ignore=true \
                                        -Dorg.slf4j.simpleLogger.showDateTime=true \
                                        -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss'
                                }
                                archiveArtifacts 'vaadin-flow/archetypes/plain-archetype/target/test-classes/projects/basic/**'
                                archiveArtifacts 'vaadin-flow/archetypes/spring-archetype/target/test-classes/projects/basic/**'

                                rtp parserName: 'HTML', nullAction: '1', stableText: """
                                    <h3>Archetypes</h3>
                                    <ul>
                                        <li><a href='${env.BUILD_URL}artifact/vaadin-flow/archetypes/plain-archetype/target/test-classes/projects/basic' target="_blank">Plain Archetype</a></li>
                                        <li><a href='${env.BUILD_URL}artifact/vaadin-flow/archetypes/spring-archetype/target/test-classes/projects/basic' target="_blank">Spring Archetype</a></li>
                                    </ul>
                                """
                            }
                        }

                        stage('SonarQube Analysis') {
                            stages {
                                stage('Analysis') {
                                    steps {
                                        withMaven() {
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
                    }
                }

                stage('Faktor-IPS Build') {
                    steps {
                        withMaven(publisherStrategy: 'EXPLICIT') {
                            sh 'mvn \
                                -Pskip-checks \
                                -pl org.linkki-framework:linkki-ips-vaadin-flow \
                                faktorips:faktorips-clean faktorips:faktorips-build'
                        }
                    }
                }

                stage('Documentation') {
                    stages {
                        stage('Check Doc links') {
                            steps {
                                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                                    sh '''
                                      if grep -R -n "/latest" vaadin-flow/doc/src/main/jbake/content/; then
                                        echo "Mentions of /latest in URLs have been found. Please use the actual version instead."
                                        exit 1
                                      else
                                        echo "OK. no mention of /latest has been found."
                                      fi
                                    '''
                                }
                            }
                        }
                        stage('Build Doc') {
                            steps {
                                withMaven(publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                                    sh 'mvn -pl vaadin-flow/doc install -Drevapi.skip -Dflatten.skip'
                                }

                                archiveArtifacts 'vaadin-flow/doc/target/doc/**'

                                rtp parserName: 'HTML', nullAction: '1', stableText: """
                                    <h3>Documentation</h3>
                                    <ul>
                                        <li><a href='${env.BUILD_URL}artifact/vaadin-flow/doc/target/doc/html/index.html' target="_blank">Documentation</a></li>
                                        <li><a href='${env.BUILD_URL}artifact/vaadin-flow/doc/target/doc/html/00_releasenotes/index.html' target="_blank">Release Notes</a></li>
                                        <li><a href='${env.BUILD_URL}artifact/vaadin-flow/doc/target/doc/html/02_tutorial/index.html' target="_blank">Tutorial</a></li>
                                    </ul>
                                """
                            }
                        }
                    }
                }

                stage('Deployment and UI Test') {
                    stages {
                        stage('Build Sample Frontend') {
                            steps {
                                withMaven(publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                                    // Rebuild samples with frontend in parallel. Separate invocation allows
                                    // parallel execution since each module runs as its own Maven process,
                                    // working around the Vaadin Maven plugin not being thread-safe.
                                    sh 'mvn -T 4 install -Pskip-checks -pl vaadin-flow/samples -amd'
                                }
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

                        stage('Wait for Server') {
                            steps {
                                waitForServer "${DEPLOYMENT_URL}"
                                waitForServer "${F10_DEPLOYMENT_URL}"
                            }
                        }

                        stage('UI Test') {
                            steps {
                                withMaven(publisherStrategy: 'EXPLICIT') {
                                    sh 'mvn \
                                        -f vaadin-flow/samples/test-playground/uitest/pom.xml \
                                        test \
                                        -Dmaven.test.failure.ignore=true \
                                        -Dsurefire.rerunFailingTestsCount=3 \
                                        -Pheadless \
                                        -Dorg.slf4j.simpleLogger.showDateTime=true \
                                        -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss'
                                }

                                archiveArtifacts allowEmptyArchive: true, artifacts: 'vaadin-flow/samples/test-playground/uitest/target/error-screenshots/*.png'
                                junit 'vaadin-flow/samples/test-playground/uitest/target/surefire-reports/*.xml'
                            }

                            post {
                                always {
                                    sh 'COUNT=$(pgrep -c -f chrome || true); pkill -9 -f chrome || true; echo "Killed ${COUNT} chrome handle(s)"'
                                }
                            }

                            environment {
                                MAVEN_OPTS = '-Xmx2g -Dtest.hostname=${DEPLOYMENT_HOST} -Dtest.port=80 -Dtest.path=${DEPLOYMENT_NAME} -Dtest.hostname.f10=${F10_DEPLOYMENT_HOST} -Dtest.port.f10=80'
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