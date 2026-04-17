pipeline {
  agent any

  tools {
    // Configure these in Jenkins: Manage Jenkins -> Tools
    jdk 'jdk17'
    maven 'maven3'
  }

  environment {
    // CHANGE THIS
    DOCKERHUB_USER = 'YOUR_DOCKERHUB_USERNAME'
    IMAGE_NAME     = 'ecommerce-oms'
    IMAGE_TAG      = "${env.BUILD_NUMBER}"
    FULL_IMAGE     = "${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

    // CHANGE THIS to your Jenkins credential id (Username/Password)
    DOCKERHUB_CREDENTIALS_ID = 'dockerhub-creds'

    // K8s namespace (optional)
    K8S_NAMESPACE = 'default'
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

    stage('Docker Build') {
      steps {
        sh "docker build -t ${FULL_IMAGE} ."
      }
    }

    stage('Docker Hub Login & Push') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
          usernameVariable: 'DH_USER',
          passwordVariable: 'DH_PASS'
        )]) {
          sh '''
            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
          '''
        }
        sh "docker push ${FULL_IMAGE}"
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        // Assumes kubectl is installed on the Jenkins agent and kubeconfig/cluster access is already set.
        // This replaces the image in your k8s/deployment.yml and applies both YAMLs.
        sh """
          sed -i 's|image: .*|image: ${FULL_IMAGE}|' k8s/deployment.yml
          kubectl -n ${K8S_NAMESPACE} apply -f k8s/deployment.yml
          kubectl -n ${K8S_NAMESPACE} apply -f k8s/service.yml
          kubectl -n ${K8S_NAMESPACE} rollout status deployment/ecommerce-oms --timeout=120s
        """
      }
    }
  }

  post {
    always {
      // Cleanup local images to save disk space on Jenkins node (optional)
      sh "docker logout || true"
    }
  }
}
