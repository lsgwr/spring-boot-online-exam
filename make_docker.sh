#!/bin/bash
rm -rf backend/src/main/resources/static
rm -rf frontend/dist
cd frontend && npm install && npm run build && mv dist ../backend/src/main/resources/static
cd ../backend && mvn clean package
cd ../ && docker build -t waterknife/exam .
