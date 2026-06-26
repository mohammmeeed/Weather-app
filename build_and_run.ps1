# build_and_run.ps1

$projectRoot = $PSScriptRoot
$libDir = Join-Path $projectRoot "lib"
$targetDir = Join-Path $projectRoot "target\classes"
$srcDir = Join-Path $projectRoot "src\main\java"

$orgJsonUrl = "https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar"
$orgJsonJar = Join-Path $libDir "json-20231013.jar"

# Create directories
if (-not (Test-Path $libDir)) { New-Item -ItemType Directory -Path $libDir | Out-Null }
if (-not (Test-Path $targetDir)) { New-Item -ItemType Directory -Path $targetDir | Out-Null }

# Download JSON dependency if it doesn't exist
if (-not (Test-Path $orgJsonJar)) {
    Write-Host "Downloading json-20231013.jar..."
    Invoke-WebRequest -Uri $orgJsonUrl -OutFile $orgJsonJar
}

Write-Host "Compiling Java sources..."
$javaFilesStr = (Get-ChildItem -Path $srcDir -Filter *.java -Recurse | Select-Object -ExpandProperty FullName) -join ' '

# Hardcode path to Adoptium JDK compiler because global javac was failing
$javacPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\javac.exe"
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\java.exe"

Invoke-Expression "& `"$javacPath`" -d `"$targetDir`" -cp `"$orgJsonJar`" $javaFilesStr"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful. Running the application..."
    Invoke-Expression "& `"$javaPath`" -cp `"$targetDir;$orgJsonJar`" com.weatherapp.WeatherApp"
} else {
    Write-Host "Compilation failed."
}
