pipeline {
    agent { label 'node-1' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    } 
    environment {
	    APP_NAME = "osc_client"
            RELEASE = "1.0.0"
            DOCKER_USER = "riyaachkarpohre"
            DOCKER_PASS = 'dckr_pat_qDwEQf_08GkgD20u-LNBMXhZpWc '
            IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
            IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
	
    }
    stages{
        stage("Cleanup Workspace"){
                steps {
                cleanWs()
                }
        }

        stage("Checkout from SCM"){
                steps {
                    git branch: 'main', credentialsId: 'github', url: 'https://github.com/RiyaAc/Jenkinstest.git'
                }
        }

	stage("Build Application"){
            steps {
                sh "mvn clean package"
            }

       }
    

         stage("Test Application"){
           steps {
                 sh "mvn test"
           }
       }

     stage("SonarQube Analysis"){
           steps {
	           script {
		        withSonarQubeEnv(credentialsId: 'jenkins-sonarqube-token') { 
                        sh "mvn sonar:sonar"
		        }
	           }	
           }
       }
    }
       
}
