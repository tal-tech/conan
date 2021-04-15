#!/bin/bash
# author dengkn
# time 2021-01-1
# ----------------------------------------------------------------------------------
# View Script for the configcenter Server
# 查看当前进程信息
# ----------------------------------------------------------------------------------

PORT="8081"
PID=`netstat -apn | grep ::$PORT | awk '{printf $7}' | cut -d/ -f1`
if test -z "$PID"
then
  echo "程序未启动,服务器无法查询相关信息!"
else
  echo "PID=$PID"
  echo "PORT=$PORT"
  echo "程序已经启动!"
  top -d 1 -p $PID;
fi
