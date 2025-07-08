library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                dir('doc-overview') {
                    withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                        sh 'mvn clean package'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                dir('doc-overview') {
                    withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                        sh 'mvn -U -T 6 \
                            clean deploy \
                            -Ddoc.user=doc'
                    }

                    archiveArtifacts 'target/doc/**'
                }
            }
        }
    }

    post {
        regression {
            sendFailureEmail()
        }
    }
}
