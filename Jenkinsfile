// Jenkinsfile (Windows agent)
pipeline {
  agent any

  tools {
    jdk 'JDK 17'
    maven 'Maven3'
  }

  environment {
    // CHANGE THESE
    DOCKERHUB_USER = 'thulasiram0'
    IMAGE_NAME     = 'ecommerce-oms'
    IMAGE_TAG      = "${env.BUILD_NUMBER}"
    FULL_IMAGE     = "${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

    // Jenkins Credentials ID for Docker Hub (Username/Password)
    DOCKERHUB_CREDENTIALS_ID = 'dockerhub-creds'

    // Kubernetes namespace
    K8S_NAMESPACE = 'default'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Unit Tests') {
      steps {
        bat 'mvn -B clean test'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Package') {
      steps {
        bat 'mvn -B -DskipTests package'
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }

    stage('Docker Build') {
      steps {
        bat 'docker version'
        bat "docker build -t %FULL_IMAGE% ."
      }
    }

    stage('Docker Hub Login & Push') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
          usernameVariable: 'DH_USER',
          passwordVariable: 'DH_PASS'
        )]) {
          // Windows docker login (safe enough for assignment use; for production use more secure approaches)
          bat 'docker logout'
          bat 'docker login -u %DH_USER% -p %DH_PASS%'
        }
        bat "docker push %FULL_IMAGE%"
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        // Requires kubectl installed + kubeconfig configured on this Jenkins machine
        // Update image in deployment YAML (Windows PowerShell)
        powershell """
          (Get-Content k8s/deployment.yml) -replace 'image: .*', 'image: ${env:FULL_IMAGE}' | Set-Content k8s/deployment.yml
          kubectl -n ${env:K8S_NAMESPACE} apply -f k8s/deployment.yml
          kubectl -n ${env:K8S_NAMESPACE} apply -f k8s/service.yml
          kubectl -n ${env:K8S_NAMESPACE} rollout status deployment/ecommerce-oms --timeout=180s
        """
      }
    }
  }
}
