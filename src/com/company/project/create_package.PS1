 # call the build script
# export the common variables from app inputs

$scriptpath = $MyInvocation.MyCommand.Path

$dir = Split-Path $scriptpath

cd $dir

cd ../..

. ./inputs.PS1


echo -n " "
echo -n " "
echo -n " "
pwd


echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
echo "Starting execution of Deploy script  ... "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"


echo "Executing C:\Windows\Microsoft.NET\Framework\v4.0.30319\MSBuild.exe $PROJECT_FILE  /p:Configuration=Release  /t:Package ... "
C:\Windows\Microsoft.NET\Framework\v4.0.30319\MSBuild.exe $PROJECT_FILE  /p:Configuration=Release  /t:Package

if ( $? )
{
echo "Deploy script is executed successfully."
exit 0
}
else
{
echo "Deploy script is FAILED."
exit 1
}

echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
echo "Execution of deploy script is completed "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"


echo -n " "
echo -n " "
echo -n " "
