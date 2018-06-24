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
	        		sh "echo hello"
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
