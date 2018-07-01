# export the common variables from app inputs

$scriptpath = $MyInvocation.MyCommand.Path

$dir = Split-Path $scriptpath

cd $dir

cd ../..

. ./inputs.yaml


pwd


Add-Type -AssemblyName System.IO.Compression.FileSystem
function Unzip
{
    param([string]$zipfile, [string]$outpath)

    [System.IO.Compression.ZipFile]::ExtractToDirectory($zipfile, $outpath)
}

echo "Executing Remove-Item D:\\Deploy1\$APP -Force -Recurse ... "
Remove-Item D:\\Deploy1\$APP -Force -Recurse

echo "Executing new-item D:\\Deploy1\$APP -itemtype directory ..."
new-item D:\\Deploy1\$APP -itemtype directory

echo "Executing Unzip \"D:\$REPO\$APP\obj\Release\Package\$PACKAGE\" \"D:\Deploy1\$APP\" ..."
Unzip "D:\$REPO\$APP\obj\Release\Package\$PACKAGE" "D:\Deploy1\$APP"

echo "Executing Remove-Item D:\\Deploy\$APP -Force -Recurse ... "
Remove-Item D:\\Deploy\$APP -Force -Recurse

echo "Executing move D:\Deploy1\$APP\Content\D_C\$REPO\$APP\obj\Release\Package\PackageTmp D:\Deploy\$APP ... "
move D:\Deploy1\$APP\Content\D_C\$REPO\$APP\obj\Release\Package\PackageTmp D:\Deploy\$APP


