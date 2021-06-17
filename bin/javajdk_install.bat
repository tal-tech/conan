if ""%1"" ==""debug"" goto needJavaHome

if not "%JRE_HOME%" == "" gotogotJreHome

if not "%JAVA_HOME%" == "" gotogotJavaHome

goto exit

:needJavaHome

if "%JAVA_HOME%" == "" gotonoJavaHome

if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome

if not exist "%JAVA_HOME%\bin\javaw.exe"goto noJavaHome

if not exist "%JAVA_HOME%\bin\jdb.exe" gotonoJavaHome

if not exist "%JAVA_HOME%\bin\javac.exe"goto noJavaHome

set "JRE_HOME=%JAVA_HOME%"

goto okJava

:noJavaHome

goto exit

:gotJavaHome

set "JRE_HOME=%JAVA_HOME%"

:gotJreHome

if not exist "%JRE_HOME%\bin\java.exe" goto noJreHome

if not exist "%JRE_HOME%\bin\javaw.exe" goto noJreHome

goto okJava

:noJreHome

goto exit

:okJava

if not "%JAVA_ENDORSED_DIRS%" == ""goto gotEndorseddir

set"JAVA_ENDORSED_DIRS=%CATALINA_HOME%\endorsed"

:gotEndorseddir

set _RUNJAVA="%JRE_HOME%\bin\java"

set _RUNJDB="%JAVA_HOME%\bin\jdb"

goto end

:exit

exit /b 1

:end

exit /b 0
