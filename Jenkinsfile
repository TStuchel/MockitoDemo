pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
         sh 'gradle --version'
         sh 'gradle clean build'
      }
    }
    stage('Test') {
      steps {
        sh 'gradle check'
      }
    }
  }
  post {
    always {
      junit 'build/reports/**/*.xml'
    }      
  }
}
