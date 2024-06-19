pipeline {
    agent { label 'linux' }
    environment {
        ARTIFACTORY_CREDS = credentials('artifactory')
    }
    stages {
        stage('Test') {
            steps {
                script {
                    sh './gradlew --info -Partifactory_user=$ARTIFACTORY_CREDS_USR -Partifactory_password=$ARTIFACTORY_CREDS_PSW coverageTestReport'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "./gradlew sonar"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    sh './gradlew --info -Partifactory_user=$ARTIFACTORY_CREDS_USR -Partifactory_password=$ARTIFACTORY_CREDS_PSW build'
                }
            }
        }

        stage('Publish') {
            steps {
                //
                script {
                    sh './gradlew --info -Partifactory_user=$ARTIFACTORY_CREDS_USR -Partifactory_password=$ARTIFACTORY_CREDS_PSW snapshot artifactoryPublish'
                }
            }
        }
    }
}
