library 'f10-jenkins-library@1.1_patches'

node {
    withMaven(maven: 'maven 3.9', jdk: 'OpenJDK 17') {
        patchDocumentation {
            documentationProject 'linkki'
            mavenModule 'vaadin-flow/doc'
        }
    }
}