pipeline {
  agent {docker { image 'openjdk:8'}}
  stages {
    stage('Run Tests') {
      steps {
        sh './mvnw clean test'
      }
    }
  }
}