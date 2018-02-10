@echo off
echo [INFO] package the war to target.
echo [INFO] --------------------
echo [INFO] grunt build begin;
cd %~dp0
cd ..
call mvn cargo:redeploy
cd bin
echo [INFO] grunt build over;
echo [INFO] --------------------
pause