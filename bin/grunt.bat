@echo off
echo [INFO] grunt build corpSysLand.
echo [INFO] --------------------
echo [INFO] grunt build begin;
cd %~dp0
cd ..
call grunt build
cd bin
echo [INFO] grunt build over;
echo [INFO] --------------------
pause