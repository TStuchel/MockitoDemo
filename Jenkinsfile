pipeline {
  agent any
  stages {
    stage('Gradle Build') {
      if (isUnix()) {
        sh './gradlew clean build'
      } else {
        bat 'gradlew.bat clean build'
      }
    }
  }
}
