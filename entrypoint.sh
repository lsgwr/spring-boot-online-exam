#!/bin/sh
systemctl start mysqld.service
nohup java -jar app.jar
