library 'f10-jenkins-library@1.1_patches'

pipeline {
    agent any

    environment {
        PROJECT_NAME = 'linkki'
        BRANCH_NAME = "${env.GIT_LOCAL_BRANCH}"
    }

    stages {
        stage('Pre-Build') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER}.${env.BRANCH_NAME}"
                }
            }
        }

        stage('Deploy Documentation') {
            steps {
                withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 17', mavenLocalRepo: '.repository', publisherStrategy: 'EXPLICIT', options: [artifactsPublisher()]) {
                    sh 'mvn -U -pl vaadin-flow/doc clean deploy -Ddoc.user=doc'
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
