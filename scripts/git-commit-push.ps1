# Generic git commit and push script
param(
    [Parameter(Mandatory=$true)]
    [string]$Message,
    [string[]]$Files = @("."),
    [string]$Branch = "main",
    [string]$Remote = "origin"
)

# Add files
if ($Files -eq "." -or $Files.Count -eq 0) {
    git add .
} else {
    git add $Files
}

# Commit
git commit -m $Message

# Push
git push $Remote $Branch

