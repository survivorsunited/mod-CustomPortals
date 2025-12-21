# Test Name: Version Bump Test
# Purpose: Tests that build.ps1 automatically bumps patch version
# Expected: Version increments from x.y.z to x.y.z+1 when build.ps1 runs
# Failure: Version doesn't change or changes incorrectly

param(
    [string]$MinecraftVersion = "1.21.8",
    [int]$TimeoutSeconds = 60
)

$ErrorActionPreference = "Stop"
$TestName = "002-test-version-bump"

# Setup logging FIRST
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptPath

# Setup isolated output folder
$testOutputDir = Join-Path $scriptPath "test-output" $TestName
New-Item -ItemType Directory -Path $testOutputDir -Force | Out-Null
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$logFile = Join-Path $testOutputDir "${MinecraftVersion}-${timestamp}.log"

function Write-TestLog {
    param([string]$Message, [string]$Color = "White")
    $ts = Get-Date -Format "HH:mm:ss"
    $msg = "[$ts] $Message"
    Write-Host $msg -ForegroundColor $Color
    Add-Content -Path $logFile -Value $msg
}

Set-Location $projectRoot

Write-TestLog "=== Version Bump Test ===" "Cyan"
Write-TestLog "Test: Verify build.ps1 automatically bumps patch version" "Yellow"

# Step 1: Get current version BEFORE build
Write-TestLog "Reading current version from gradle.properties..." "Yellow"
$gradlePropsBefore = Get-Content gradle.properties -Raw
if ($gradlePropsBefore -match "mod_version\s*=\s*([0-9]+\.[0-9]+\.[0-9]+)") {
    $beforeVersion = $matches[1]
    Write-TestLog "Version before build: $beforeVersion" "Cyan"
} else {
    Write-TestLog "❌ FAILED: Could not find mod_version in gradle.properties" "Red"
    exit 1
}

# Parse version parts to calculate expected version
$versionParts = $beforeVersion -split '\.'
if ($versionParts.Count -ne 3) {
    Write-TestLog "❌ FAILED: Invalid version format: $beforeVersion" "Red"
    exit 1
}

$expectedMajor = [int]$versionParts[0]
$expectedMinor = [int]$versionParts[1]
$expectedPatch = [int]$versionParts[2] + 1
$expectedVersion = "$expectedMajor.$expectedMinor.$expectedPatch"

Write-TestLog "Expected version after build: $expectedVersion" "Cyan"

# Step 2: Run build.ps1 (this should bump the version)
Write-TestLog "Running build.ps1 (should bump version from $beforeVersion to $expectedVersion)..." "Yellow"
$buildOutput = .\build.ps1 -MinecraftVersion $MinecraftVersion 2>&1
$buildOutput | Add-Content -Path $logFile
$buildExitCode = $LASTEXITCODE

if ($buildExitCode -ne 0) {
    Write-TestLog "⚠️ Build exited with code $buildExitCode, but continuing version check..." "Yellow"
}

# Step 3: Check version AFTER build
Write-TestLog "Reading version from gradle.properties after build..." "Yellow"
$gradlePropsAfter = Get-Content gradle.properties -Raw
if ($gradlePropsAfter -match "mod_version\s*=\s*([0-9]+\.[0-9]+\.[0-9]+)") {
    $afterVersion = $matches[1]
    Write-TestLog "Version after build: $afterVersion" "Cyan"
    
    # Step 4: Verify version was bumped correctly
    if ($afterVersion -eq $expectedVersion) {
        Write-TestLog "✅ PASSED: Version correctly bumped from $beforeVersion to $afterVersion" "Green"
        Write-TestLog "Result: PASSED" "Green"
        exit 0
    } elseif ($afterVersion -eq $beforeVersion) {
        Write-TestLog "❌ FAILED: Version did NOT change!" "Red"
        Write-TestLog "   Before: $beforeVersion" "Red"
        Write-TestLog "   After:  $afterVersion" "Red"
        Write-TestLog "   Expected: $expectedVersion" "Red"
        Write-TestLog "Result: FAILED" "Red"
        exit 1
    } else {
        Write-TestLog "❌ FAILED: Version changed incorrectly!" "Red"
        Write-TestLog "   Before: $beforeVersion" "Red"
        Write-TestLog "   After:  $afterVersion" "Red"
        Write-TestLog "   Expected: $expectedVersion" "Red"
        Write-TestLog "Result: FAILED" "Red"
        exit 1
    }
} else {
    Write-TestLog "❌ FAILED: Could not read version after build" "Red"
    Write-TestLog "Result: FAILED" "Red"
    exit 1
}
