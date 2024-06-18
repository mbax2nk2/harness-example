pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                scripts {
                    echo "build"
                    version = sh (returnStdout: true, script:'./gradlew snapshot projectVersion').trim()
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
