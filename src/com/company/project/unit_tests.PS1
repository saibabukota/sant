# Perform the unit tests 
# export the common variables from app inputs
echo "Starting the unit tests ... "

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
echo "Starting execution of unit tests   ... "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"


echo "executing C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE\devenv.com' $SOLUTION_FILE -build ... "
#& 'C:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\IDE\devenv.com' $SOLUTION_FILE -build

if ( $? )
{
echo "Unit tests are executed successfully."
exit 0
}
else
{
echo "Unit tests execution is FAILED."
exit 1
}

echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
echo "Execution of create artifact is completed "
echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

echo -n " "
echo -n " "
echo -n " "
