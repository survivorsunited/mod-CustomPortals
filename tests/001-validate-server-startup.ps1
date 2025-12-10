# Test 001: Server Startup Validation
# Purpose: Validates server starts, world loads, mod loads without errors
# Expected: Server starts, world loads, no errors in logs
# Failure: Server crashes, world fails to load, errors detected

param(
    [string]$MinecraftVersion = "1.21.8",
    [int]$TimeoutSeconds = 180
)

$ErrorActionPreference = "Stop"
$TestName = "001-validate-server-startup"

# Setup logging - isolated folder per test
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptPath
$testOutputDir = Join-Path $scriptPath "test-output" $TestName
if (-not (Test-Path $testOutputDir)) {
    New-Item -ItemType Directory -Path $testOutputDir -Force | Out-Null
}

$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$logFile = Join-Path $testOutputDir "${MinecraftVersion}-${timestamp}.log"

function Write-TestLog {
    param([string]$Message)
    $ts = Get-Date -Format "HH:mm:ss"
    $msg = "[$ts] $Message"
    Write-Host $msg
    Add-Content -Path $logFile -Value $msg
}

Set-Location $projectRoot

Write-TestLog "TEST: $TestName - Minecraft $MinecraftVersion"

# Step 1: Build mod
Write-TestLog "Building mod..."
.\build.ps1 -MinecraftVersion $MinecraftVersion 2>&1 | Add-Content -Path $logFile
if ($LASTEXITCODE -ne 0) {
    Write-TestLog "BUILD FAILED"
    exit 1
}

# Copy build artifacts to test output
Write-TestLog "Copying build artifacts to test-output..."
$buildLibs = Join-Path $projectRoot "build/libs"
if (Test-Path $buildLibs) {
    $buildArtifactsDir = Join-Path $testOutputDir "build-artifacts"
    New-Item -ItemType Directory -Path $buildArtifactsDir -Force | Out-Null
    Copy-Item -Path "$buildLibs\*.jar" -Destination $buildArtifactsDir -Force -ErrorAction SilentlyContinue
    Write-TestLog "Build artifacts copied to test-output"
}

# Step 2: Setup and start server using build.ps1 -StartServer
Write-TestLog "Setting up and starting server..."
$serverDir = Join-Path $testOutputDir "test-server"
if (Test-Path $serverDir) {
    Remove-Item -Path $serverDir -Recurse -Force -ErrorAction SilentlyContinue
}

# Start build.ps1 -StartServer in background, but we'll move server to test-output after setup
$tempServerDir = "test-server"
if (Test-Path $tempServerDir) {
    Remove-Item -Path $tempServerDir -Recurse -Force -ErrorAction SilentlyContinue
}

# Start build.ps1 -StartServer in background
$serverJob = Start-Job -ScriptBlock {
    param($root, $version)
    Set-Location $root
    .\build.ps1 -StartServer -MinecraftVersion $version
} -ArgumentList $projectRoot, $MinecraftVersion

Write-TestLog "Server job started (ID: $($serverJob.Id))"

# Step 3: Monitor server logs (monitor temp directory while server runs)
Write-TestLog "Monitoring server logs..."
$serverStarted = $false
$worldLoaded = $false
$errorFound = $false
$startTime = Get-Date
# Monitor temp server directory while it runs, will copy to test-output later
$serverLogFile = Join-Path $tempServerDir "server.log"

while ((Get-Date) -lt $startTime.AddSeconds($TimeoutSeconds)) {
    Start-Sleep -Seconds 2
    
    if (-not (Test-Path $serverLogFile)) {
        continue
    }
    
    $logContent = Get-Content -Path $serverLogFile -Tail 100 -ErrorAction SilentlyContinue
    
    if (($logContent -match "Done.*For help" -or $logContent -match "Done \(") -and -not $serverStarted) {
        Write-TestLog "Server started"
        $serverStarted = $true
    }
    
    if (($logContent -match "Loading dimension" -or $logContent -match "Preparing start region" -or $logContent -match "Preparing level" -or $logContent -match "Preparing spawn area") -and -not $worldLoaded -and $serverStarted) {
        Write-TestLog "World loaded"
        $worldLoaded = $true
    }
    
    if ($logContent -match "FATAL|Cannot load|Failed to load|Exception.*loading|CustomPortals.*error|Portal.*failed|PersistentState.*error|Mixin.*failed") {
        Write-TestLog "ERROR DETECTED"
        $logContent | Select-Object -Last 20 | ForEach-Object { Write-TestLog "  $_" }
        $errorFound = $true
        break
    }
    
    if ($serverStarted -and $worldLoaded) {
        Write-TestLog "SUCCESS"
        break
    }
}

# Step 4: Cleanup
Write-TestLog "Stopping server..."
if ($serverJob) {
    Stop-Job -Job $serverJob -ErrorAction SilentlyContinue
    Remove-Job -Job $serverJob -Force -ErrorAction SilentlyContinue
}
Get-Process -Name "java" -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue

# Copy ALL server artifacts to test-output
Write-TestLog "Copying server artifacts to test-output..."
if (Test-Path $tempServerDir) {
    $serverArtifactsDir = Join-Path $testOutputDir "server"
    Copy-Item -Path "$tempServerDir\*" -Destination $serverArtifactsDir -Recurse -Force -ErrorAction SilentlyContinue
    Write-TestLog "Server artifacts copied to test-output"
}

# Also copy from moved location if it exists
if (Test-Path $serverDir) {
    $serverArtifactsDir = Join-Path $testOutputDir "server"
    Copy-Item -Path "$serverDir\*" -Destination $serverArtifactsDir -Recurse -Force -ErrorAction SilentlyContinue
}

# Results
$testPassed = $serverStarted -and $worldLoaded -and -not $errorFound
Write-TestLog "Result: ServerStarted=$serverStarted WorldLoaded=$worldLoaded Errors=$errorFound"
Write-TestLog "Test Result: $(if ($testPassed) { 'PASSED' } else { 'FAILED' })"

exit $(if ($testPassed) { 0 } else { 1 })
