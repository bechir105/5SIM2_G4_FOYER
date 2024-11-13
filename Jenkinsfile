pipeline {
    agent any

    environment {
        GIT_HTTP_BUFFER_SIZE = '524288000'

    }

    stages {
        stage('Getting code from GITHUB') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'MohamedAzizElarbi_5sim2_G4',
                    url: 'https://github.com/bechir105/5SIM2_G4_FOYER.git'
            }
        }

        stage('MVN clean') {
            steps {
                echo 'Running Maven clean...'
                sh 'mvn clean'
            }
        }

        stage('MVN package') {
            steps {
                echo 'Running Maven package...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('MVN build') {
            steps {
                echo 'Running Maven install...'
                sh 'mvn install -DskipTests'
            }
        }

        stage('MVN compile') {
            steps {
                echo 'Running Maven compile...'
                sh 'mvn compile'
            }
        }


    }

    post {
        always {
            echo 'Cleaning up...'
        }
    }
}
