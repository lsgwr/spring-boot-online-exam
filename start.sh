#!/bin/bash
docker rm -f exam
docker run -d -p 80:9527 -p 3306:3306 --name exam --privileged=true waterknife/exam  /usr/sbin/init
docker exec exam /lsg/entrypoint.sh