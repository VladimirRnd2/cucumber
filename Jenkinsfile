pipeline {
  agent { docker { image 'openjdk:11'}}
  stages {
    stage('Run Tests') {
      steps {
        sh './mvnw clean test'
      }
    }
  }
}