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
            emailext to: '${REGRESSION_EMAIL_LINKKI}', mimeType: 'text/html', subject: 'Jenkins Build Failure - $PROJECT_NAME', body: '''
                <img src="https://jenkins.io/images/logos/fire/fire.png" style="max-width: 300px;" alt="Jenkins is not happy about it ...">
                <br>
                $BUILD_URL
            '''
        }
    }
}
