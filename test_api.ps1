$javacPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\javac.exe"
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.5.8-hotspot\bin\java.exe"
$targetDir = "target\classes"
$sqliteJar = "lib\json-20231013.jar"

Write-Host "Compiling TestAPI..."
$javaFilesStr = (Get-ChildItem -Path src\main\java -Filter *.java -Recurse | Select-Object -ExpandProperty FullName) -join ' '
Invoke-Expression "& `"$javacPath`" -d `"$targetDir`" -cp `"$sqliteJar`" $javaFilesStr"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Running TestAPI..."
    Invoke-Expression "& `"$javaPath`" -cp `"$targetDir;$sqliteJar`" com.weatherapp.TestAPI"
} else {
    Write-Host "Compile failed."
}
