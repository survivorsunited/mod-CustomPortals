# Monitor GitHub Actions pipeline without requiring gh CLI
param(
    [string]$Repository = "survivorsunited/mod-CustomPortals",
    [string]$Workflow = "build.yml",
    [int]$MaxWait = 1800,  # 30 minutes
    [int]$CheckInterval = 30  # Check every 30 seconds
)

$repoOwner = $Repository.Split('/')[0]
$repoName = $Repository.Split('/')[1]

Write-Host "🔍 Monitoring pipeline for $Repository" -ForegroundColor Cyan
Write-Host "   Workflow: $Workflow" -ForegroundColor Gray
Write-Host "   Max wait: $MaxWait seconds ($([math]::Round($MaxWait/60, 1)) minutes)" -ForegroundColor Gray
Write-Host "   Check interval: $CheckInterval seconds" -ForegroundColor Gray
Write-Host ""

$elapsed = 0
$lastStatus = ""

while ($elapsed -lt $MaxWait) {
    try {
        # Get latest workflow run using GitHub API
        $headers = @{
            "Accept" = "application/vnd.github+json"
            "X-GitHub-Api-Version" = "2022-11-28"
        }
        
        # Try to get workflow runs (public repo, no auth needed)
        $url = "https://api.github.com/repos/$Repository/actions/workflows/$Workflow/runs?per_page=1"
        $response = Invoke-RestMethod -Uri $url -Headers $headers -Method Get -ErrorAction Stop
        
        if ($response.workflow_runs.Count -gt 0) {
            $run = $response.workflow_runs[0]
            $status = $run.status
            $conclusion = $run.conclusion
            $runUrl = $run.html_url
            
            $statusDisplay = switch ($status) {
                "completed" { 
                    if ($conclusion -eq "success") { "✅ SUCCESS" } 
                    elseif ($conclusion -eq "failure") { "❌ FAILED" }
                    elseif ($conclusion -eq "cancelled") { "⚠️ CANCELLED" }
                    else { "❓ $conclusion" }
                }
                "in_progress" { "🔄 RUNNING" }
                "queued" { "⏳ QUEUED" }
                default { "❓ $status" }
            }
            
            $color = switch ($status) {
                "completed" { 
                    if ($conclusion -eq "success") { "Green" } else { "Red" }
                }
                default { "Yellow" }
            }
            
            if ($statusDisplay -ne $lastStatus) {
                Write-Host "[$([math]::Round($elapsed/60, 1)) min] $statusDisplay - $($run.display_title)" -ForegroundColor $color
                Write-Host "   URL: $runUrl" -ForegroundColor Gray
                $lastStatus = $statusDisplay
            }
            
            if ($status -eq "completed") {
                if ($conclusion -eq "success") {
                    Write-Host "`n✅ Pipeline completed successfully!" -ForegroundColor Green
                    Write-Host "   View run: $runUrl" -ForegroundColor Gray
                    exit 0
                } else {
                    Write-Host "`n❌ Pipeline failed!" -ForegroundColor Red
                    Write-Host "   View run: $runUrl" -ForegroundColor Gray
                    Write-Host "`nChecking for specific errors..." -ForegroundColor Yellow
                    
                    # Try to get job details
                    try {
                        $jobsUrl = "https://api.github.com/repos/$Repository/actions/runs/$($run.id)/jobs"
                        $jobsResponse = Invoke-RestMethod -Uri $jobsUrl -Headers $headers -Method Get -ErrorAction Stop
                        
                        $failedJobs = $jobsResponse.jobs | Where-Object { $_.conclusion -eq "failure" }
                        if ($failedJobs) {
                            Write-Host "`nFailed jobs:" -ForegroundColor Red
                            foreach ($job in $failedJobs) {
                                Write-Host "  - $($job.name)" -ForegroundColor Yellow
                                Write-Host "    URL: $($job.html_url)" -ForegroundColor Gray
                            }
                        }
                    } catch {
                        Write-Host "   Could not fetch job details (may require authentication)" -ForegroundColor Yellow
                    }
                    
                    exit 1
                }
            }
        } else {
            Write-Host "[$([math]::Round($elapsed/60, 1)) min] No workflow runs found" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "[$([math]::Round($elapsed/60, 1)) min] Error checking status: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    Start-Sleep -Seconds $CheckInterval
    $elapsed += $CheckInterval
}

if ($elapsed -ge $MaxWait) {
    Write-Host "`n⏱️  Timeout waiting for pipeline to complete" -ForegroundColor Yellow
    exit 1
}

exit 0

