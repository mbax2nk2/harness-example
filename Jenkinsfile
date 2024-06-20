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
                script {
                    def scannerHome = tool 'SonarScanner 4.0';
                    withSonarQubeEnv('sonarqube') {
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
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
        post {
            always {
                junit 'build/reports/**/test_report.xml'
            }
        }
    }
}
