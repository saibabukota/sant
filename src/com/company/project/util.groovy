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


	public def PowerShell(psCmd) {
	    psCmd=psCmd.replaceAll("%", "%%")
	        bat "powershell.exe -NonInteractive -ExecutionPolicy Bypass -Command \"\$ErrorActionPreference='Stop';[Console]::OutputEncoding=[System.Text.Encoding]::UTF8;$psCmd;EXIT \$global:LastExitCode\""
		}

	public def buildSourceCode(arg1,arg2,arg3) {
		echo "Building the Source code. executing script from Common repo ... "
		print "XYZ"
		print arg1
		print arg2
		print arg3
		print "XYZ"

		echo "Starting the poweshell script for building ... "
#PowerShell(". '.\\C:\\WebApplication1\\build.PS1 arg1 arg2 arg3'")
	}
	public def executeUnitTests() {
		echo "Executing the Unit tests. executing script from Common repo ... "
#		PowerShell(". '.\\unit_tests.PS1'")
	}
	public def uploadToArtifactory() {
		echo "Uploading to Artifactory. executing script from Common repo ... "
#		PowerShell(". '.\\upload_artifact.PS1'")
	}
	public def create_package() {
		echo "Starting the poweshell script for creating package ... "
#		PowerShell(". '.\\create_package.PS1'")
	}
	public def deploy() {
		echo "Starting the poweshell script for extracting packages into web deploy directory ... "
#		PowerShell(". '.\\deploy.PS1'")
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
