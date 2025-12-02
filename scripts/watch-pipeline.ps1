# Watch pipeline until completion
Write-Host "Watching pipeline..." -ForegroundColor Cyan
$maxWait = 600  # 10 minutes max
$elapsed = 0
$checkInterval = 15

while ($elapsed -lt $maxWait) {
    $run = gh run list --workflow=build.yml --limit 1 --json databaseId,status,conclusion,displayTitle | ConvertFrom-Json
    
    $status = if ($run.status -eq "completed") { 
        if ($run.conclusion -eq "success") { "✅ SUCCESS" } else { "❌ FAILED" }
    } else { "🔄 RUNNING" }
    
    Write-Host "[$elapsed s] $status - $($run.displayTitle)" -ForegroundColor $(if ($run.status -eq "completed") { if ($run.conclusion -eq "success") { "Green" } else { "Red" } } else { "Yellow" })
    
    if ($run.status -eq "completed") {
        if ($run.conclusion -eq "failure") {
            Write-Host "`nChecking errors..." -ForegroundColor Red
            .\get-pipeline-errors.ps1
        }
        break
    }
    
    Start-Sleep -Seconds $checkInterval
    $elapsed += $checkInterval
}

if ($elapsed -ge $maxWait) {
    Write-Host "Timeout waiting for pipeline" -ForegroundColor Yellow
}


