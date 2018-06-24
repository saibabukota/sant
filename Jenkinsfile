#!/bin/groovy
	//import groovy.json.*
    def server = Artifactory.server 'ART'
    def rtMaven = Artifactory.newMavenBuild()
    def releaseRepo = 'generic-local'
    def buildInfo
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
                git url: 'root@master:/prog/cac/git/cacin-lizard.git', branch: 'release_branch'
            }
        }
	    stage ('Artifactory configuration') {
	    	steps {
	    		script {
			        rtMaven.tool = 'maven' // Tool name from Jenkins configuration
			        rtMaven.deployer releaseRepo: releaseRepo, server: server
			        buildInfo = Artifactory.newBuildInfo()
			        buildInfo.env.capture = true
			    }
	        }
	    }
        stage('Build war file') {
            steps {
				//sh "mvn clean package -Dbuild.number=${env.BUILD_NUMBER}"
	    		script {
	        		rtMaven.run pom: 'pom.xml', goals: 'clean package -Dbuild.number=${env.BUILD_NUMBER}', buildInfo: buildInfo
	        	}
            }
        }
        stage('SonarQube Analysis'){
        	steps {
	        	withSonarQubeEnv('SonarQube Server') {
	        		//sh "mvn sonar:sonar"
	        		script {
	        			rtMaven.run pom: 'pom.xml', goals: 'sonar:sonar'
	        		}
	        	}
        	}
        }
        stage('Quality Gate'){
        	steps {
	        	timeout(time: 1, unit: 'HOURS') {
		        	script {
		        		def qg = waitForQualityGate()
		        		if (qg.status != 'OK') {
		        			error "Pipeline aborted due to SonarQube quality gate failure: ${qg.status}"
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
	        		server.publishBuildInfo buildInfo
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
                sh "ansible-playbook -i 'master,' ansible/lizardbasketapiimage.yml --extra-vars 'BUILD_NUMBER=${env.BUILD_NUMBER} WORKSPACE=${env.WORKSPACE} WAR_PATH=target'"
            }
        }
        stage('Promotion to FT/UAT') {
            steps {
            	input message: 'Deploy to FT/UAT?', submitter: 'emdnakh'
                //input message: 'Deploy to FT/UAT?', submitter: 'user_uat_1'
                echo 'Deploy docker image to FT/UAT'
                sh "cd ${env.WORKSPACE}"
                sh "ansible-playbook -i 'master,' ansible/lizardbasketapicontainer.yml -u root --extra-vars 'BUILD_NUMBER=${env.BUILD_NUMBER}'"
            }
        }
        stage('Test webservice') {
            steps {
                sh "cd ${env.WORKSPACE}"
                sh "ansible-playbook -i 'master,' ansible/test.yml -u root --extra-vars 'BUILD_NUMBER=${env.BUILD_NUMBER} WORKSPACE=${env.WORKSPACE}'"
            }
        }
        stage('Promotion to Production Cluster') {
            steps {
                input message: 'Deploy to Production Cluster?', submitter: 'emdnakh'
                echo 'Deploy docker image to production cluster'
                sh "cd ${env.WORKSPACE}"
                sh "ansible-playbook -i 'manager,' ansible/lizardbasketapiswarm.yml -u root --extra-vars 'BUILD_NUMBER=${env.BUILD_NUMBER} WORKSPACE=${env.WORKSPACE}'"
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
