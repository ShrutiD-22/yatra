
pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
    }

    tools {
        maven 'mvn_3.9.9'
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Starting Code Compilation...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }
        stage('Code QA Execution') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully!'
            }
        }
     stage('SonarQube Code Quality') {
                environment {
                    scannerHome = tool 'qube'
                }
                steps {
                    echo 'Starting SonarQube Code Quality Scan...'
                    withSonarQubeEnv('sonar-server') {
                        sh 'mvn sonar:sonar'
                    }
                    echo 'SonarQube Scan Completed. Checking Quality Gate...'
                    timeout(time: 10, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                    echo 'Quality Gate Check Completed!'
                }
            }
        stage('Code Package') {
            steps {
                echo 'Creating WAR Artifact...'
                sh 'mvn clean package'
                echo 'WAR Artifact Created Successfully!'
            }
        }
        stage('Build & Tag Docker Image') {
            steps {
                echo 'Building Docker Image with Tags...'
                sh "docker build -t shrutidigraskar22/yatra:latest -t yatra:latest ."
                echo 'Docker Image Build Completed!'
            }
        }
        stage('Docker Image Scanning') {
            steps {
                echo 'Scanning Docker Image with Trivy...'
                sh 'trivy image ${DOCKER_IMAGE}:latest || echo "Scan Failed - Proceeding with Caution"'
                echo 'Docker Image Scanning Completed!'
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhubCred', variable: 'dockerhubCred')]) {
                        sh 'docker login docker.io -u shrutidigraskar22 -p ${dockerhubCred}'
                        echo 'Pushing Docker Image to Docker Hub...'
                        sh 'docker push shrutidigraskar22/yatra:latest'
                        echo 'Docker Image Pushed to Docker Hub Successfully!'
                    }
                }
            }
        }
        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://145023110537.dkr.ecr.ap-south-1.amazonaws.com"]) {
                        echo 'Tagging and Pushing Docker Image to ECR...'
                        sh '''
                            docker images
                            docker tag shrutidigraskar22/yatra:latest 145023110537.dkr.ecr.ap-south-1.amazonaws.com/yatra:latest
                            docker push 145023110537.dkr.ecr.ap-south-1.amazonaws.com/yatra:latest
                        '''
                        echo 'Docker Image Pushed to Amazon ECR Successfully!'
                    }
                }
            }
        }
        stage('Upload Docker Image to Nexus') {
                steps {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            sh 'docker login http://3.110.219.55:8085/repository/yatra/ -u admin -p ${PASSWORD}'
                            echo "Push Docker Image to Nexus : In Progress"
                            sh 'docker tag yatra 3.110.219.55:8085/yatra:latest'
                            sh 'docker push 3.110.219.55:8085/yatra'
                            echo "Push Docker Image to Nexus : Completed"
                        }
                    }
                }
            }

    }
}