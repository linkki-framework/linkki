library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    parameters {
        string(
            name: 'COMPARE_URL_TEST',
            description: 'URL of the playground deployment under test (up to and including /playground)',
            trim: true
        )
        string(
            name: 'COMPARE_URL_REFERENCE',
            defaultValue: 'http://linkki-master.dockerhost.i.faktorzehn.de/linkki-sample-test-playground-vaadin-flow/playground',
            description: 'URL of the reference playground deployment to compare against',
            trim: true
        )
    }

    tools {
        jdk 'OpenJDK 21'
        maven 'maven 3.9'
    }

    stages {
        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Playground Compare Test') {
            steps {
                withMaven(publisherStrategy: 'EXPLICIT') {
                    sh """mvn \
                        -f vaadin-flow/samples/test-playground/playwright-compare/pom.xml \
                        test \
                        -Dcompare.url.test=${params.COMPARE_URL_TEST} \
                        -Dcompare.url.reference=${params.COMPARE_URL_REFERENCE} \
                        -Dmaven.test.failure.ignore=true"""
                }

                archiveArtifacts allowEmptyArchive: true,
                    artifacts: 'vaadin-flow/samples/test-playground/playwright-compare/target/playwright-compare/**'

                junit allowEmptyResults: true,
                    testResults: 'vaadin-flow/samples/test-playground/playwright-compare/target/surefire-reports/*.xml'

                script {
                    rtp parserName: 'HTML', nullAction: '1', stableText: """
                        <h3>Playground Compare Report</h3>
                        <ul>
                            <li><a href='${env.BUILD_URL}artifact/vaadin-flow/samples/test-playground/playwright-compare/target/playwright-compare/report.html' target="_blank">Comparison Report</a></li>
                            <li><b>Test:</b> ${params.COMPARE_URL_TEST}</li>
                            <li><b>Reference:</b> ${params.COMPARE_URL_REFERENCE}</li>
                        </ul>
                    """
                }
            }
        }
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
    }
}
