#!/bin/groovy
	//import groovy.json.*
	def notifySuccess() {
		emailext (
			subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: '${JELLY_SCRIPT, template="html"}',
			mimeType: 'text/html',
			to: "santoshdevops@company.com",
			from: "DevOps COE <devops.local.smtp@gmail.com>",
			replyTo: "santoshdevops@company.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}

	def notifyFailure() {
		emailext (
			subject: "Failure: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: '${JELLY_SCRIPT, template="html"}',
			mimeType: 'text/html',
			to: "santoshdevops@company.com",
			from: "DevOps COE <devops.local.smtp@gmail.com>",
			replyTo: "santoshdevops@company.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}

	def emailDownloadLink(String artifactUrl) {
		emailext (
			subject: "Build Release: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: "URL: $artifactUrl",
			mimeType: 'text/html',
			to: "santoshdevops@company.com",
			from: "DevOps COE <devops.local.smtp@gmail.com>",
			replyTo: "santoshdevops@company.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}


pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/vinss1/NewSampleApplication.git', branch: 'master'
            }
        }
	    stage ('Artifactory configuration') {
	    	steps {
	    		script {
			        print "artifactory"
			    }
	        }
	    }
        stage('Build war file') {
            steps {
				//sh "mvn clean package -Dbuild.number=${env.BUILD_NUMBER}"
	    		script {
	        		rtMaven.run pom: 'pom.xml', goals: 'clean ', buildInfo: buildInfo
	        	}
            }
        }
        stage('SonarQube Analysis'){
        	steps {
	        	withSonarQubeEnv('SonarQube Server') {
	        		//sh "mvn sonar:sonar"
	        		script {
	        			rtMaven.run pom: 'pom.xml', goals: 'clean'
	        		}
	        	}
        	}
        }
        stage('Quality Gate'){
        	steps {
	        	timeout(time: 1, unit: 'HOURS') {
		        	script {
		        	echo "Hello"
		        		}
		        	}
	        	}
	        }
        }
	    //stage ('Upload articats to Artifactory') {
	    	//steps {
	    		//script {
					//def uploadSpec = """{
						//"files": [
							//{
								//"pattern": "${env.WORKSPACE}/target/*.war",
								//"target": "generic-local/cacin-lizard/"
							//}
						//]
					//}"""
					//server.upload(uploadSpec)
			    //}
	        //}
	    //}
	    stage ('Publish build info to Artifactory') {
	    	steps {
	    		script {
	        	echo "Hello"
	        	}
	        }
	    }
	    stage ('Email build package download link') {
	    	steps {
	    		script {
	    			emailDownloadLink(server.url+'/'+releaseRepo+'/'+buildInfo.deployableArtifacts.get(0).artifactPath)
	        	}
	        }
	    }
        stage('Build docker image and upload to docker repository') {
            steps {
                sh "cd ${env.WORKSPACE}"
              sh "echo hi"
						}
        }
        stage('Promotion to FT/UAT') {
            steps {
            	input message: 'Deploy to FT/UAT?', submitter: 'emdnakh'
                //input message: 'Deploy to FT/UAT?', submitter: 'user_uat_1'
                echo 'Deploy docker image to FT/UAT'
                sh "cd ${env.WORKSPACE}"
								sh "echo hello"

            }
        }
        stage('Test webservice') {
            steps {
                sh "cd ${env.WORKSPACE}"
								sh "echo hi"

            }
        }

    }
	post {
        //success {
        	//script {
        		//notifySuccess()
        	//}
        //}
        failure {
        	script {
        		notifyFailure()
        	}
        }
    }
}
