@echo off
REM ============================================================
REM  Chay ung dung Quan ly Sinh vien
REM  Tu dong bien dich neu chua co thu muc build\
REM ============================================================
chcp 65001 >nul
setlocal

set LIB=lib\mysql-connector-j-8.4.0.jar

if not exist build (
    echo Chua co ban bien dich. Dang bien dich...
    if exist sources.txt del sources.txt
    for /r src %%f in (*.java) do echo %%f>> sources.txt
    mkdir build
    javac -encoding UTF-8 -d build -cp "%LIB%" @sources.txt
    if errorlevel 1 (
        echo *** BIEN DICH THAT BAI ***
        pause
        exit /b 1
    )
)

echo Dang chay ung dung...
java -Dfile.encoding=UTF-8 -cp "build;%LIB%" com.qlsv.Main
