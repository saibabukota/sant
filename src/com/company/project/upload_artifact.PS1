# export the common variables from app inputs
echo "Starting the build for web application ... "

$scriptpath = $MyInvocation.MyCommand.Path

$dir = Split-Path $scriptpath

cd $dir

cd ../..
. ./inputs.yaml

echo -n " "
echo -n " "
echo -n " "

pwd


# call the create artifact


echo -n " ";

echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
echo "Starting execution of create artifact XYZ ... "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"


echo "executing C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE\devenv.com' $SOLUTION_FILE -build ... "
#& 'C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE\devenv.com' $SOLUTION_FILE -build

if ( $? )
{
echo "Upload artifact script is executed successfully."
exit 0
}
else
{
echo "Upload artifact script is FAILED."
exit 1
}

echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
echo "Execution of create artifact is completed "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

echo -n " "
echo -n " "
echo -n " "
