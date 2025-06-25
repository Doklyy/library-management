@echo off
echo Testing Library Management Statistics APIs with Authentication
echo =============================================================

echo.
echo 1. Login to get JWT token:
echo --------------------------
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}" > login_response.json

echo.
echo Login response saved to login_response.json

echo.
echo 2. Extract JWT token and test Books by Category Statistics:
echo -----------------------------------------------------------
for /f "tokens=*" %%i in ('powershell -Command "(Get-Content login_response.json | ConvertFrom-Json).token"') do set JWT_TOKEN=%%i
curl -X GET "http://localhost:8080/api/statistics/books-by-category" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%"

echo.
echo.
echo 3. Test Top Liked Posts Statistics:
echo -----------------------------------
curl -X GET "http://localhost:8080/api/statistics/top-liked-posts" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%"

echo.
echo.
echo API Testing Complete!
echo Cleaning up temporary files...
del login_response.json
pause 