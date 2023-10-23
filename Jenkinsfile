pipeline {
    agent { label 'node-1' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    } 
    environment {
	    APP_NAME = "ri_jenkins"
            RELEASE = "1.0.0"
            DOCKER_USER = "riyaachkarpohre"
            DOCKER_PASS = "dckr_pat_xb8M5ExYIF5q-Y7MoOmaifbM0a0"
            IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
            IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
	    JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
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

	    
          stage("Build & Push Docker Image") {
             steps {
                   script {
                         // Define your Docker image name and tag
                         def dockerImageName = "${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG}"
			   

                       // Build the Docker image
                       //def dockerImage = sudo docker.build(dockerImageName, "-f Dockerfile .")
			 sh "sudo docker build -t ${dockerImageName} ."
			   

                      // Push the Docker image to the registry
                      //dockerImage.withRegistry([credentialsId: 'dckr_pat__q6AAb1T_91GS7Pne5MBpHXKIRk', url: 'https://your-docker-registry.com']) {
                     // dockerImage.withRegistry([credentialsId: 'dckr_pat__q6AAb1T_91GS7Pne5MBpHXKIRk', url: 'https://hub.docker.com/u/riyaachkarpohre']
		     //  dockerImage.withRegistry([credentialsId: 'dckr_pat__q6AAb1T_91GS7Pne5MBpHXKIRk', url: ' ']
				//		{dockerImage.push()}
			// dockerImage([credentialsId: 'dckr_pat__q6AAb1T_91GS7Pne5MBpHXKIRk', url: ' ']
			// dockerImage.push()		       
			//   sh "sudo docker push ${dockerImageName}"
		    sh "sudo docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
		    sh "sudo docker push ${dockerImageName}"	   
               } 
            }
         }        
       
	stage("Trivy Scan") {
            steps {
                script {
                    sh ("sudo docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${IMAGE_NAME}:${IMAGE_TAG} --no-progress --scanners vuln  --exit-code 0 --severity HIGH,CRITICAL --format table")
                }
            }
        }

        stage ('Cleanup Artifacts') {
            steps {
                script {
		    sh "sudo docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
                }
            }
        }
	stage("Trigger CD Pipeline") {
            steps {
                script {
                    sh "curl -v -k --user clouduser:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'ec2-54-85-26-183.compute-1.amazonaws.com:8080/job/gitops-register-app-cd/buildWithParameters?token=gitops-token'"
                } 
            }
       }    
    }

    post {
        failure {
            emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
            subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Failed", 
            mimeType: 'text/html',to: "riya.a@technobase.in"
        }
        success {
            emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
            subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Successful", 
            mimeType: 'text/html',to: "riya.a@technobase.in"
        }      
    }
  }

