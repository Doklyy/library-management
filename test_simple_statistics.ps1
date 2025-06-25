# Simple test for Statistics APIs
Write-Host "Simple Statistics API Test" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green

# Test without authentication first
Write-Host "`n1. Testing Books by Category (without auth):" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/statistics/books-by-category" -Method GET -ContentType "application/json"
    Write-Host "Success! Response:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "`n2. Testing Top Liked Posts (without auth):" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/statistics/top-liked-posts" -Method GET -ContentType "application/json"
    Write-Host "Success! Response:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Test with curl command
Write-Host "`n3. Testing with curl:" -ForegroundColor Yellow
Write-Host "Books by Category:" -ForegroundColor Cyan
curl -X GET "http://localhost:8080/api/statistics/books-by-category" -H "Content-Type: application/json"

Write-Host "`nTop Liked Posts:" -ForegroundColor Cyan
curl -X GET "http://localhost:8080/api/statistics/top-liked-posts" -H "Content-Type: application/json"

Write-Host "`nTest Complete!" -ForegroundColor Green 