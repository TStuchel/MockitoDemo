pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                sh 'git clean -f'
                sh 'git reset --hard origin/master'
            }
        }
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
        stage('Quality') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh 'gradle check'
                    }
                    post {
                        always {
                            junit 'build/test-results/**/*.xml'
                        }
                    }
                }
                stage('SonarQube') {
                    steps {
                        withSonarQubeEnv('SonarQube') {
                            sh 'gradle --info sonarqube'
                        }
                    }
                }
            }
        }
    }
}
