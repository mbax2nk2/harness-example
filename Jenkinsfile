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
                    sh './gradlew --stacktrace -Partifactory_user=$ARTIFACTORY_CREDS_USR -Partifactory_password=$ARTIFACTORY_CREDS_PSW snapshot projectVersion'

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
