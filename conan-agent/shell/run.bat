#!/bin/bash
# author dengkun
# time 2021-01-02


cd %~dp0
cd ../conan-agent/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -jar %JAVA_OPTS% conan-agent.jar

cd bin


