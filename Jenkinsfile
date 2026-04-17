pipeline {
  agent any

  tools {
    jdk 'JDK 17'
    maven 'Maven3'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Unit Tests') {
      steps { sh 'mvn -B clean test' }
      post { always { junit 'target/surefire-reports/*.xml' } }
    }

    stage('Package') {
      steps { sh 'mvn -B -DskipTests package' }
      post { success { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true } }
    }
  }
}
