#!/bin/bash
# author dengkn
# time 2021-01-02

# ----------------------------------------------------------------------------------
# Stop Script for the configcenter Server
# 停止进程，移植其他模块的时候注意修改进程启动的端口号（PORT）
# ----------------------------------------------------------------------------------

# Get the PID by port
PORT="8082"
PID=`netstat -apn | grep ::$PORT | awk '{printf $7}' | cut -d/ -f1`
# Kill process by the PID
kill -9 $PID
