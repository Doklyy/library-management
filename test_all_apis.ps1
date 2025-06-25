# Test All Library Management APIs
Write-Host "Testing All Library Management APIs" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green

# Function to test API
function Test-API {
    param(
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [string]$Body = $null
    )
    
    Write-Host "`n$Name" -ForegroundColor Yellow
    Write-Host "URL: $Url" -ForegroundColor Cyan
    
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $Body -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -ContentType "application/json"
        }
        
        Write-Host "✅ SUCCESS" -ForegroundColor Green
        if ($response) {
            Write-Host "Response: $($response | ConvertTo-Json -Depth 2)" -ForegroundColor White
        }
    } catch {
        Write-Host "❌ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        }
    }
}

# Test Statistics APIs
Write-Host "`n📊 STATISTICS APIs" -ForegroundColor Magenta
Test-API -Name "Books by Category Statistics" -Url "http://localhost:8080/api/statistics/books-by-category"
Test-API -Name "Top Liked Posts Statistics" -Url "http://localhost:8080/api/statistics/top-liked-posts"

# Test Book APIs
Write-Host "`n📚 BOOK APIs" -ForegroundColor Magenta
Test-API -Name "Get All Books" -Url "http://localhost:8080/api/books"
Test-API -Name "Get Book Categories" -Url "http://localhost:8080/api/books/categories"
Test-API -Name "Book Category Statistics" -Url "http://localhost:8080/api/books/statistics/category"

# Test Category APIs
Write-Host "`n🏷️ CATEGORY APIs" -ForegroundColor Magenta
Test-API -Name "Get All Categories" -Url "http://localhost:8080/api/categories"

# Test Borrow APIs
Write-Host "`n📖 BORROW APIs" -ForegroundColor Magenta
Test-API -Name "Get All Borrows" -Url "http://localhost:8080/api/borrows"

# Test Post APIs
Write-Host "`n📝 POST APIs" -ForegroundColor Magenta
Test-API -Name "Get All Posts" -Url "http://localhost:8080/api/posts"

# Test Comment APIs
Write-Host "`n💬 COMMENT APIs" -ForegroundColor Magenta
Test-API -Name "Get Comments by Post ID" -Url "http://localhost:8080/api/comments/post/1"

# Test User APIs
Write-Host "`n👤 USER APIs" -ForegroundColor Magenta
Test-API -Name "Get All Users" -Url "http://localhost:8080/api/users"

Write-Host "`n🎉 API Testing Complete!" -ForegroundColor Green 