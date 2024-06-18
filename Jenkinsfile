pipeline {
    agent any
    environment {
        ARTIFACTORY_CREDS = credentials('artifactory')
    }
    stages {
        stage('Build') {
            steps {
                script {
                    echo "build"
                    version = sh (returnStdout: true, script:'./gradlew -Partifactory_user=$ARTIFACTORY_CREDS_USR -Partifactory_password=$ARTIFACTORY_CREDS_PSW snapshot projectVersion').trim()
                    echo "$version"
                }
            }
        }
        stage('Test') {
            steps {
                //
                echo "test"
            }
        }
        stage('Deploy') {
            steps {
                //
                echo "deploy"
            }
        }
    }
}
