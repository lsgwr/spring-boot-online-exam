#!/bin/bash
docker rm -f mysql
docker rm -f exam-backend-frontend
docker run -d -p 3306:3306 --name mysql --privileged=true waterknife/centos-nginx-jdk8-mysql  /usr/sbin/init
docker run -d --network=host --name exam-backend-frontend waterknife/exam
