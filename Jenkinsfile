pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
         sh 'gradle --version'
         sh './gradlew clean build'
      }
    }
    stage('Test') {
      steps {
        sh './gradlew check'
      }
    }
  }
  post {
    always {
      junit 'build/test-results/**/*.xml'
    }      
  }
}
