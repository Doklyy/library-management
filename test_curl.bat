@echo off
echo Testing Statistics APIs with curl
echo =================================

echo.
echo 1. Testing Books by Category:
curl -X GET "http://localhost:8080/api/statistics/books-by-category"

echo.
echo.
echo 2. Testing Top Liked Posts:
curl -X GET "http://localhost:8080/api/statistics/top-liked-posts"

echo.
echo Test Complete!
pause 