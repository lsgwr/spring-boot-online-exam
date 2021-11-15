#!/bin/bash
cd frontend/exam/ && npm install && npm run build && mv dist ../../backend/exam/src/main/resources/static
cd ../../backend/exam/ && mvn clean package
cd ../../ && docker build -t waterknife/exam .
