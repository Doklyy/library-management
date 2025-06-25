# Test Library Management Statistics APIs
Write-Host "Testing Library Management Statistics APIs" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green

# Step 1: Login to get JWT token
Write-Host "`n1. Logging in to get JWT token..." -ForegroundColor Yellow
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $loginResponse.token

Write-Host "Login successful! Token received." -ForegroundColor Green

# Step 2: Test Books by Category Statistics
Write-Host "`n2. Testing Books by Category Statistics..." -ForegroundColor Yellow
try {
    $booksByCategory = Invoke-RestMethod -Uri "http://localhost:8080/api/statistics/books-by-category" -Method GET -Headers @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    Write-Host "Books by Category Statistics:" -ForegroundColor Green
    $booksByCategory | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting books by category: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test Top Liked Posts Statistics
Write-Host "`n3. Testing Top Liked Posts Statistics..." -ForegroundColor Yellow
try {
    $topLikedPosts = Invoke-RestMethod -Uri "http://localhost:8080/api/statistics/top-liked-posts" -Method GET -Headers @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    Write-Host "Top Liked Posts Statistics:" -ForegroundColor Green
    $topLikedPosts | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting top liked posts: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nAPI Testing Complete!" -ForegroundColor Green 