#!/bin/groovy

package com.company.project;

	public def getVersionNumber(String versionFilePath) {
		def props = readProperties file: versionFilePath
		return props['appVersion']
	}

	public def getCommitMessage() {
		def commitMessage = sh (script: "git log --format=%B -n 1  ", returnStdout: true)
		return commitMessage
	}
	
	
	public def buildSourceCode() {
		echo "Building the Source code. executing script from Common repo ... "
	}
	public def executeUnitTests() {
		echo "Executing the Unit tests. executing script from Common repo ... "
	}
	public def uploadToArtifactory() {
		echo "Uploading to Artifactory. executing script from Common repo ... "
	}
	public def deploy() {
		echo "Deploying to Servers. executing script from Common repo ... "
	}

	public void writeVersionToFile(String versionFilePath, String version) {
		Properties props = new Properties()
		File propsFile = new File(versionFilePath)
		DataInputStream dis = propsFile.newDataInputStream()
		BufferedWriter bw = propsFile.newWriter()
		props.load(dis)
		props.setProperty('appVersion', version)
		props.store(bw, null)
		bw.close()
		dis.close()
	}

	public def incrementVersion(String version, String message) {
		message = message.substring(0,2)
		def versionSplit = version.split('\\.')
		def us = versionSplit[0]
		def cr = versionSplit[1]
		def tr = versionSplit[2]
		if(message.equalsIgnoreCase("us")) {
			us = us.toInteger()+1
			us = us.toString()
			if(us.length()>1) {
				version = us+".00.00"
			}
			else {
				version = "0"+us+".00.00"
			}
		}
		else if(message.equalsIgnoreCase("cr")) {
			cr = cr.toInteger()+1
			cr = cr.toString()
			if(cr.length()>1) {
				version = us+"."+cr+".00"
			}
			else {
				version = us+".0"+cr+".00"
			}
		}
		else if(message.equalsIgnoreCase("tr")) {
			tr = tr.toInteger()+1
			tr = tr.toString()
			if(tr.length()>1) {
				version = us+"."+cr+"."+tr
			}
			else {
				version = us+"."+cr+".0"+tr
			}
		}
		return version
	}

	public void notifySuccess() {
		emailext (
			subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: '${JELLY_SCRIPT, template="html"}',
			mimeType: 'text/html',
			to: "vallab.v@gmail.com",
			from: "company project DevOps <project.devops@company.com>",
			replyTo: "vallab.v@gmail.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}

	public void notifyFailure() {
		emailext (
			subject: "Failure: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: '${JELLY_SCRIPT, template="html"}',
			mimeType: 'text/html',
			to: "vallab.v@gmail.com",
			from: "company project DevOps <project.devops@company.com>",
			replyTo: "vallab.v@gmail.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'CulpritsRecipientProvider'],
				//[$class: 'FailingTestSuspectsRecipientProvider'], [$class: 'RequesterRecipientProvider'], [$class: 'UpstreamComitterRecipientProvider']]
		)
	}

	public void emailDownloadLink(String artifactUrl) {
		emailext (
			subject: "Build Release: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: "URL: $artifactUrl",
			mimeType: 'text/html',
			to: "vallab.v@gmail.com",
			from: "company project DevOps <project.devops@company.com>",
			replyTo: "vallab.v@gmail.com",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}

	public void gerritWorkflowNotification(String to, String from, String replyTo) {
		emailext (
			subject: "Build Release: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			body: "Build is verified by Jenkins successfully",
			mimeType: 'text/html',
			to: "$to",
			from: "$from",
			replyTo: "$replyTo",
			//recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}
	public void qualityGateVote(String gerritServer, String gerritPort, String change, String patch, String lable, String message) {
		sh " ssh -p ${gerritPort} jenkins@${gerritServer} gerrit review ${change},${patch}  --${lable} +1 --message=${message}"
	}
