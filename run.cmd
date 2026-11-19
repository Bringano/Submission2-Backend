@echo off

call mvnw.cmd clean package
if errorlevel 1 exit /b 1

java -jar target\carservice-0.0.1-SNAPSHOT.jar