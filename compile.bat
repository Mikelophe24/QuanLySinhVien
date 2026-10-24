@echo off
REM ============================================================
REM  Bien dich toan bo ma nguon Java vao thu muc build\
REM ============================================================
chcp 65001 >nul
setlocal

set LIB=lib\mysql-connector-j-8.4.0.jar

echo [1/2] Tao danh sach file nguon...
if exist sources.txt del sources.txt
for /r src %%f in (*.java) do echo %%f>> sources.txt

echo [2/2] Bien dich (UTF-8)...
if not exist build mkdir build
javac -encoding UTF-8 -d build -cp "%LIB%" @sources.txt
if errorlevel 1 (
    echo.
    echo *** BIEN DICH THAT BAI ***
    pause
    exit /b 1
)

echo.
echo Bien dich thanh cong! Class nam trong thu muc build\
pause
