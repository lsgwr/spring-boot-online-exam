#!/bin/bash
cd backend/exam/ && mvn clean package
cd ../../frontend/exam/ && npm install && npm run build
cd ../../ && docker build -t waterknife/exam .
