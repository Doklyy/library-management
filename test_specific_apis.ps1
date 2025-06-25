# Test Specific APIs that are having issues
Write-Host "Testing Specific APIs" -ForegroundColor Green
Write-Host "=====================" -ForegroundColor Green

# Function to test API with detailed error info
function Test-API-Detailed {
    param(
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [string]$Body = $null
    )
    
    Write-Host "`nTesting: $Name" -ForegroundColor Yellow
    Write-Host "URL: $Url" -ForegroundColor Cyan
    Write-Host "Method: $Method" -ForegroundColor Cyan
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            Write-Host "Body: $Body" -ForegroundColor Gray
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $Body
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        Write-Host "SUCCESS (200)" -ForegroundColor Green
        if ($response) {
            $jsonResponse = $response | ConvertTo-Json -Depth 3
            if ($jsonResponse.Length -gt 200) {
                Write-Host "Response (truncated): $($jsonResponse.Substring(0, 200))..." -ForegroundColor White
            } else {
                Write-Host "Response: $jsonResponse" -ForegroundColor White
            }
        }
    } catch {
        Write-Host "ERROR" -ForegroundColor Red
        Write-Host "Message: $($_.Exception.Message)" -ForegroundColor Red
        
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode
            Write-Host "Status Code: $statusCode" -ForegroundColor Red
            
            # Try to get response body for more details
            try {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $responseBody = $reader.ReadToEnd()
                Write-Host "Response Body: $responseBody" -ForegroundColor Red
                $reader.Close()
            } catch {
                Write-Host "Could not read response body" -ForegroundColor Red
            }
        }
    }
}

# Test Comments API
Write-Host "`nTESTING COMMENTS API" -ForegroundColor Magenta
Test-API-Detailed -Name "Get Comments by Post ID" -Url "http://localhost:8080/api/comments/post/1"
Test-API-Detailed -Name "Get All Comments" -Url "http://localhost:8080/api/comments"

# Test Categories API
Write-Host "`nTESTING CATEGORIES API" -ForegroundColor Magenta
Test-API-Detailed -Name "Get All Categories" -Url "http://localhost:8080/api/categories"
Test-API-Detailed -Name "Get Category by ID" -Url "http://localhost:8080/api/categories/1"

# Test Borrows API
Write-Host "`nTESTING BORROWS API" -ForegroundColor Magenta
Test-API-Detailed -Name "Get All Borrows" -Url "http://localhost:8080/api/borrows"

# Test Statistics API
Write-Host "`nTESTING STATISTICS API" -ForegroundColor Magenta
Test-API-Detailed -Name "Books by Category Statistics" -Url "http://localhost:8080/api/statistics/books-by-category"
Test-API-Detailed -Name "Top Liked Posts Statistics" -Url "http://localhost:8080/api/statistics/top-liked-posts"

Write-Host "`nTesting Complete!" -ForegroundColor Green 