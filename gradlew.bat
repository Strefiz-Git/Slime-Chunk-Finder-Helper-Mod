@if "%DEBUG%"=="" @echo off
@setlocal enabledelayedexpansion

if exist "%~dp0\gradle.wrapper\gradle-wrapper.jar" set GRADLE_WRAPPER_JAR="%~dp0\gradle.wrapper\gradle-wrapper.jar"
if exist "%~dp0\..\gradle\wrapper\gradle-wrapper.jar" set GRADLE_WRAPPER_JAR="%~dp0\..\gradle\wrapper\gradle-wrapper.jar"

if not defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute

:findJavaFromJavaHome
set JAVA_EXE=java.exe
call :findJavaInPath
if not defined JAVA_EXE goto error

:execute
%JAVA_EXE% -Dorg.gradle.appname=Gradle -classpath "%GRADLE_WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*

:end
@endlocal
goto :eof

:error
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
exit /b 1

:findJavaInPath
set PATH_VAR=%PATH%
:loop
if "%PATH_VAR%"=="" goto end
for /f "tokens=1* delims=;" %%a in ("%PATH_VAR%") do (
    set PATH_VAR=%%b
    if exist "%%a\java.exe" set JAVA_EXE=%%a\java.exe & goto execute
)
goto loop
