@echo off
echo [INFO] package the war to target.

cd %~dp0
cd ..
call mvn clean yuicompressor:compress
pause