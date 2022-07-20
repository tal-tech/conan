#!/bin/bash
# author dengkun
# time 2021-01-02

nohup java -jar conan-agent.jar --spring.profiles.active=test >/dev/null 2>&1&
