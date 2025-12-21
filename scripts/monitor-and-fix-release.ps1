# Monitor pipeline and automatically fix issues until release succeeds
# Usage: .\scripts\monitor-and-fix-release.ps1 -Version "1.1.0"

param(
    [string]$Version = "1.1.0",
    [int]$MaxIterations = 50,
    [int]$CheckInterval = 60
)

Write-Host "🚀 Starting continuous monitoring and fixing for release $Version" -ForegroundColor Cyan
Write-Host "   Will retry up to $MaxIterations times" -ForegroundColor Gray
Write-Host "   Check interval: $CheckInterval seconds" -ForegroundColor Gray
Write-Host ""

$iteration = 0
$success = $false

while ($iteration -lt $MaxIterations -and !$success) {
    $iteration++
    $elapsed = [math]::Round($iteration * $CheckInterval / 60, 1)
    
    Write-Host "[Iteration $iteration / $elapsed min] Checking pipeline status..." -ForegroundColor Cyan
    
    # Use monitor-pipeline script to check status
    try {
        $url = "https://api.github.com/repos/survivorsunited/mod-CustomPortals/actions/workflows/build.yml/runs?per_page=1"
        $headers = @{
            "Accept" = "application/vnd.github+json"
            "X-GitHub-Api-Version" = "2022-11-28"
        }
        
        $response = Invoke-RestMethod -Uri $url -Headers $headers -ErrorAction Stop
        $run = $response.workflow_runs[0]
        $status = $run.status
        $conclusion = $run.conclusion
        
        Write-Host "   Status: $status / $conclusion" -ForegroundColor $(if ($status -eq 'completed') { 
            if ($conclusion -eq 'success') { 'Green' } else { 'Red' } 
        } else { 'Yellow' })
        
        if ($status -eq 'completed') {
            if ($conclusion -eq 'success') {
                Write-Host "`n🎉🎉🎉 SUCCESS! Pipeline is GREEN! 🎉🎉🎉" -ForegroundColor Green
                Write-Host "   Release $Version should have artifacts now!" -ForegroundColor Green
                Write-Host "   Run: .\scripts\check-release.ps1 -Tag `"$Version`"" -ForegroundColor Cyan
                $success = $true
                break
            } else {
                Write-Host "`n❌ Pipeline failed. Checking errors and retrying..." -ForegroundColor Red
                
                # Get failed jobs
                try {
                    $jobsUrl = "https://api.github.com/repos/survivorsunited/mod-CustomPortals/actions/runs/$($run.id)/jobs"
                    $jobsResponse = Invoke-RestMethod -Uri $jobsUrl -Headers $headers -ErrorAction Stop
                    $failedJobs = $jobsResponse.jobs | Where-Object { $_.conclusion -eq 'failure' }
                    
                    if ($failedJobs) {
                        Write-Host "   Failed jobs:" -ForegroundColor Yellow
                        $failedJobs | ForEach-Object {
                            Write-Host "     - $($_.name)" -ForegroundColor Red
                        }
                        
                        # If release-manual failed, recreate tag
                        $releaseFailed = $failedJobs | Where-Object { $_.name -eq 'release-manual' }
                        if ($releaseFailed) {
                            Write-Host "`n   Recreating tag $Version to trigger new release..." -ForegroundColor Yellow
                            
                            # Delete and recreate tag
                            git tag -d $Version 2>&1 | Out-Null
                            git push origin ":refs/tags/$Version" 2>&1 | Out-Null
                            Start-Sleep -Seconds 2
                            git tag -a $Version -m "Release $Version"
                            git push origin $Version 2>&1 | Out-Null
                            
                            Write-Host "   ✅ Tag recreated. Waiting for new pipeline run..." -ForegroundColor Green
                            Start-Sleep -Seconds 30
                            $iteration = 0  # Reset counter for new run
                        }
                    }
                } catch {
                    Write-Host "   Could not get job details: $($_.Exception.Message)" -ForegroundColor Yellow
                }
            }
        } else {
            Write-Host "   Pipeline still running, waiting..." -ForegroundColor Yellow
        }
    } catch {
        if ($_.Exception.Message -match "rate limit") {
            Write-Host "   ⚠️  Rate limited, waiting longer..." -ForegroundColor Yellow
            Start-Sleep -Seconds 120
        } else {
            Write-Host "   ⚠️  Error checking status: $($_.Exception.Message)" -ForegroundColor Yellow
        }
    }
    
    if (!$success) {
        Start-Sleep -Seconds $CheckInterval
    }
}

if (!$success) {
    Write-Host "`n⚠️  Reached max iterations. Pipeline may still be running or needs manual intervention." -ForegroundColor Yellow
    Write-Host "   Check status: .\scripts\monitor-pipeline.ps1" -ForegroundColor Cyan
    exit 1
} else {
    Write-Host "`n✅ Monitoring complete! Release should be ready." -ForegroundColor Green
    exit 0
}













