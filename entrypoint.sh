#!/bin/sh
nginx
nohup java -jar app.jar --spring.profiles.active=prod

