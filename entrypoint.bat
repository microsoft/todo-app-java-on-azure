@echo off
cd /d %~dp0

set PATH=C:\sffabricbin;C:\Code;%PATH% 

C:\SFFabricBin\vc14_redist.x64.exe -quiet
C:\SFFabricBin\vcredist_x64.exe -quiet
set > env.log
java -Djava.library.path=%PATH% -jar todo-app-java-on-azure-1.0-SNAPSHOT.jar  >>  javaprocess.log

start /wait timeout 600