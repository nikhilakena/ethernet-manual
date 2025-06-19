@REM Script starting a wiremock server using mappings in src/main/resources/mock directory
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir

set SCRIPT_DIR=%~dp0
set MOCK_PORT=9980
set MOCK_ROOT_DIR=%SCRIPT_DIR%src\main\mock

call mvnw dependency:copy-dependencies -DincludeGroupIds=com.github.tomakehurst -DincludeArtifactIds=wiremock-standalone

for /f %%a in ('dir /b target\dependency\wiremock*.jar') do (
  call set "WIREMOCK_JAR=%%a"
)

java -jar %SCRIPT_DIR%target\dependency\%WIREMOCK_JAR% --port=%MOCK_PORT% --root-dir=%MOCK_ROOT_DIR%
