node()
	{
    try
		{
		stage'select the build type'

	    if (env.BRANCH_NAME == 'develop')
			{
			 print "Building the develop branch "
			//calling the function developBranch if develop branch is getting built
			 developBranch()
			}

	    else if (env.BRANCH_NAME == 'master')
			{
			 print "Building the master branch "
			//calling the function masterBranch if master branch is getting built
			 masterBranch()
			}
	    else
			{
			 print "Building the feature branch"
			//calling the function featureBranch if feature branch is getting built
			 featureBranch()
			}

		}
	catch(e)
		{
		currentBuild.result = "FAILED"
		throw(e)
		}
    finally
		{
		notifyBuild(currentBuild.result)
		}
	}

// function definition for developBranch
//// build using maven, run the tests, push it to artifactory with SNAPSHOT-1.0.0 as the version
def developBranch()
	{
//Download code from github_repo
    stage'download_github_code'
		download_github_code()
//perform maven clean
    stage'clean'
		clean()
//perform maven junit tests
	stage'junit_tests'
		junit_tests()
//publish the coverage report and junit reports
	stage'publish_results'
		publish_junit()
		publish_html()
//create zip package from the source code and push it to artifactory
    stage'package_publish_artifactory'
		package_publish_artifactory()
// call the function packerjob, it will extact artifact from artifactory, validate packer , and runs packer build to create customized AMI with Application
//call the downstream job - deploy to AWS using packer and terraforms

	stage'call_downstream_cdjob'
		call_downstream_cdjob()

    }


// function definition for masterBranch

// build using maven, run the tests, push it to artifactory with jenkins build number as the version
def masterBranch()
	{
//Download code from github_repo
	stage'download_github_repo_code'
		download_github_repo_code()
//perform maven clean
	stage'clean'
		clean()
//perform maven junit tests
	stage'junit_tests'
		junit_tests()
//publish the coverage report and junit reports
	stage'publish_results'
		publish_junit()
		publish_html()
//create zip package from the source code and push it to artifactory
  	stage'package_publish_artifactory_master'
		package_publish_artifactory_master()
// call the function packerjob, it will extact artifact from artifactory, validate packer , and runs packer build to create customized AMI with Application
   packerjob()

	}

// function defination for featureBranch
////// build using maven, run the tests, send mails to team.

def featureBranch()
	{
//Download code from github_repo
    stage'download_github_repo_code'
		download_github_repo_code()
//perform maven clean
    stage'clean'
		clean()
//perform maven junit tests
	stage'junit_tests'
		junit_tests()
//publish the coverage report and junit reports
	stage'publish_results'
		publish_junit()

	}



// Function definitions start from here

//Function definition for downloading code from github_repo to workspace in jenkins
def download_github_repo_code()
	{
	sh "echo clone github_repo for develop branch"
	checkout scm
	}
//Function definition to perform maven clean
def clean()
	{
	sh "mvn clean"
	}
//Function definition to perform maven tests
def junit_tests()
	{
	sh "mvn test | tee filejunittest"
	}
//Function definition to publish the junit results
	def publish_junit()
	{

	echo "Execute unit tests"

	}
//Function definition to publish the html coverage report on the jenkins page
def publish_html()
	{
		echo "publish htmp reports "

	}
//Function definition to push the artifacts to artifactory
def package_publish_artifactory()
	{
	sh """
	mvn -DskipTests	deploy
	"""
	}
//Function definition to push the artifacts to artifactory 	for the master branch, perorms maven release
def package_publish_artifactory_master()
	{

		def originalV = get_version();
	    def major = originalV[1];
        def minor = originalV[2];
        def patch  = originalV[3];
        def pom_version = "${major}.${minor}.${patch}";
		def commit_message = sh (script: "git log -1| tail -1", returnStdout: true)


		load "input.properties"

		sh """
		mvn -DskipTests	package
		curl -H 'X-JFrog-Art-Api: ${artifactory_apikey}' -T target/${appname}-${pom_version}.zip  ${artifactory_host}/${artifactory_path}/${appname}/${pom_version}/${appname}-${pom_version}.zip

		git config user.name "us-mcd-se-pilot"
		git config user.email "us-mcd-se-pilot@us.mcd.com"
		git tag v$pom_version.${env.BUILD_NUMBER}
		git push -u ${repo_artifactory_path} master tag v$pom_version.${env.BUILD_NUMBER}
		"""
	}

def packerjob() {
	stage 'Extract App from Artifactory'
		//Extract App from Artifactory
		sh """
		cd packer
		sh download_artifact.sh
		"""
	stage 'Validate packer'
		sh """
		cd packer
		export appname=`echo dcs-*.zip |cut -f1,2,3 -d "-"`
		export release=`echo dcs-*.zip | cut -f4 -d"-" | cut -f1,2,3 -d"."`
		export BUILD_NUMBER
		/opt/hashicorp/packer validate -var-file variables.json packer.json
		"""
	stage 'Run packer build'
		sh """#!/bin/bash
		export AWS_PROFILE="mcd-dcs"
		export aws_access_key
		export aws_secret_key
		cd packer
		export appname=`echo dcs-*.zip |cut -f1,2,3 -d "-"`
		export release=`echo dcs-*.zip | cut -f4 -d"-" | cut -f1,2,3 -d"."`
		export BUILD_NUMBER
		/home/ec2-user/terra/packer build --debug -var-file variables.json packer.json |tee /tmp/packer.log
		"""
	}












//Function definition to call the downsteram stage which is cloud hub deployment for MW
def call_downstream_cdjob()
	{
		build job: '/call-terraform'


	}
//get the pom version from pom file

def get_version()
	{
		def matcher = readFile('pom.xml') =~ '<version>(\\d*)\\.(\\d*)\\.(\\d*)(-SNAPSHOT)*</version>'
		matcher ? matcher[0] : null
	}

//send out mail with build results, job url etc
def notifyBuild(buildStatus=currentBuild.result)
	{
	buildStatus =  buildStatus ?: 'SUCCESSFUL'
	def commit_message = sh (script: "git log -1| sed 1d ", returnStdout: true)
	print commit_message
	def total_tests = sh (script: " cat filejunittest | grep -A30 'junit Coverage Summary' | grep Tests | tail -1 | cut -f2 -d:", returnStdout: true)
	def total_errors = sh (script: " cat filejunittest | grep -A30 'junit Coverage Summary' | grep Errors | tail -1 | cut -f2 -d:", returnStdout: true)
	def total_failed = sh (script: " cat filejunittest | grep -A30 'junit Coverage Summary' | grep Failures | tail -1 | cut -f2 -d:", returnStdout: true)
	def total_skipped = sh (script: " cat filejunittest | grep -A30 'junit Coverage Summary' | grep Skipped | tail -1 | cut -f2 -d:", returnStdout: true)
	def test_result  =  sh (script: "cat filejunittest | grep BUILD | cut -f3 -d' '", returnStdout: true)


	// Default values
	def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
	emailext (subject: subject,
	body: 	""" <p> ${subject} </p>
		<p>INTEGRATED junit TESTS SUMMARY </p>
		<p><b><u> Release Notes: (${commit_message}) </u></b></p>
		<p>Total number of tests executed = (${total_tests}) </p>
		<p>Total number of test errors = (${total_errors}) </p>
		<p>Total number of tests failed = (${total_failed}) </p>
		<p>Total number of tests skipped = (${total_skipped}) </p>
		<p>Test Result = (${test_result}) </p>
		<p>Check Console Log at (${env.BUILD_URL}) <p>
		<p>Check junit coverage report at  (${env.BUILD_URL}/Coverage_Report) </p>  """,
		to: 'US-MCD_SE_Pilot@us.mcd.com' ,
		mimeType: 'text/html'
	)
	}
