#!/bin/bash

##########################
#                        #
# @Com: TAL EP           #
# @Auchor: zhangyongxin5 #
# @Date: 2021.6.21       #
#                        #
##########################

function checkUser(){
    currUser=`echo $USER`
    if [ $currUser != "root" ];then
        echo "current user is $currUser, please use root!"
        exit 1
    fi
}

function changeMaxMapCount(){
    curr=`sysctl -a|grep vm.max_map_count`
    echo "ready to modify 655360"
    # temporary
    now=`sysctl -w vm.max_map_count=655360`
    # permanent
    # vim /etc/sysctl.conf vm.max_map_count=655360
    if [[ $now =~ 655360 ]];then
        echo "modify end"
        echo "now is `sysctl -a|grep vm.max_map_count`"
    else
        echo "modify failed"
    fi
}

function checkDockerEnv(){
    comm=`docker -v`
    if [[ $comm =~ "command not found" ]];then
        echo "docker command is not installed"
        echo "please install docker:"
        echo "Ubuntu: curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun"
        echo "CentOS: curl -fsSL https://get.docker.com | bash -s docker --mirror aliyun"
        echo "MacOS: brew install --cask --appdir=/Applications docker"
        exit 1
    fi
}

function getImageAndStart(){
    docker pull eptal/conan:latest
    # 5601: kibana
    # 6379: redis
    # 3306: mysql
    # 2181: zookeeper client
    # 8080: zookeeperAdmin
    # 9092: kafka
    # 9200: es
    # 8081: conan admin
    # 8082: conan agent
    # 8090: conan fe
    randomStr=`cat /dev/urandom | head -n 10 | md5sum | head -c 10`
    startRes=`docker run -itd --name conan-${randomStr} -p5601:5601 -p 6379:6379 -p 3306:3306 -p 2181:2181 -p 9092:9092 -p 9200:9200 -p 8080:8080 -p 8081:8081 -p 8082:8082 -p 8090:8090 eptal/conan`
    if [[ $startRes =~ "port is already" ]];then
        echo "start failed, please check the port is occupied"
        exit 1
    fi
    echo "containerId: " $startRes
    echo "enter the container command is: docker exec -it containerId /bin/bash"
}

function main(){
    checkUser
    changeMaxMapCount
    checkDockerEnv
    getImageAndStart
}

main