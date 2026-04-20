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
            when {
                branch 'master'
            }
            steps {
                dir('doc-overview') {
                    withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 21', publisherStrategy: 'EXPLICIT') {
                        sh 'mvn -U clean install'
                        uploadDocumentation project: 'linkki-version-overview', folder: 'target/doc/html', legacyMode: false, updateLatest: true
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
