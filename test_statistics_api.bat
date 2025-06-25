@echo off
echo Testing Library Management Statistics APIs
echo ==========================================

echo.
echo 1. Testing Books by Category Statistics:
echo ----------------------------------------
curl -X GET "http://localhost:8080/api/statistics/books-by-category" -H "Content-Type: application/json"

echo.
echo.
echo 2. Testing Top Liked Posts Statistics:
echo --------------------------------------
curl -X GET "http://localhost:8080/api/statistics/top-liked-posts" -H "Content-Type: application/json"

echo.
echo.
echo API Testing Complete!
pause 