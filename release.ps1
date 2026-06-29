Param(
  [Parameter(Mandatory=$true)]
  [string]$Version
)

$releaseBranch = "v1.21.11"
$tagName = "v1.21.11-$Version"

# Ensure we're on the dedicated 1.21.11 release branch
$currentBranch = git branch --show-current
if ($currentBranch -ne $releaseBranch) {
    Write-Host "⚠️  Not on $releaseBranch branch. Current branch: $currentBranch" -ForegroundColor Yellow
    Write-Host "Switching to $releaseBranch branch..." -ForegroundColor Cyan
    git checkout $releaseBranch
}

git pull origin $releaseBranch

# Update gradle.properties with the new version and keep the branch pinned to 1.21.11
Write-Host "📝 Updating gradle.properties with version $Version for Minecraft 1.21.11" -ForegroundColor Cyan
$gradleProps = Get-Content gradle.properties -Raw
$gradleProps = $gradleProps -replace "minecraft_version\s*=\s*[^\r\n]*", "minecraft_version=1.21.11"
$gradleProps = $gradleProps -replace "yarn_mappings\s*=\s*[^\r\n]*", "yarn_mappings=1.21.11+build.3"
$gradleProps = $gradleProps -replace "loader_version\s*=\s*[^\r\n]*", "loader_version=0.17.3"
$gradleProps = $gradleProps -replace "fabric_loader_version\s*=\s*[^\r\n]*", "fabric_loader_version=0.17.3"
$gradleProps = $gradleProps -replace "loom_version\s*=\s*[^\r\n]*", "loom_version=1.12.0-alpha.25"
$gradleProps = $gradleProps -replace "fabric_version\s*=\s*[^\r\n]*", "fabric_version=0.141.3+1.21.11"
$gradleProps = $gradleProps -replace "mod_version\s*=\s*[^\r\n]*", "mod_version = $Version"
Set-Content gradle.properties -Value $gradleProps -NoNewline

# Commit the version change if needed
$null = git diff --quiet gradle.properties
if ($LASTEXITCODE -ne 0) {
    Write-Host "📝 Version/build pins updated in gradle.properties" -ForegroundColor Green
    git add gradle.properties
    git commit -m "Release $tagName"
    git push origin $releaseBranch
    Write-Host "✅ Committed release prep to $releaseBranch" -ForegroundColor Green
} else {
    Write-Host "ℹ️  gradle.properties already set for $tagName" -ForegroundColor Gray
}

# Recreate the release tag if it already exists
$tagExists = git tag -l $tagName
if ($tagExists) {
    Write-Host "Tag $tagName already exists. Recreating it..." -ForegroundColor Yellow
    git tag -d $tagName
    git push origin ":refs/tags/$tagName" 2>&1 | Out-Null
}

Write-Host "Creating release tag: $tagName" -ForegroundColor Cyan
git tag -a $tagName -m "Release $tagName"
git push origin $tagName

Write-Host "✅ Tag $tagName pushed successfully" -ForegroundColor Green
Write-Host "The v1.21.11 workflow will build and publish the GitHub release." -ForegroundColor Yellow
