@echo off
echo [INFO] Package the war in target dir.

cd %~dp0
cd ..
call mvn clean package -DskipTests=true
cd bin
pause