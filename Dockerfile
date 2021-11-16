FROM waterknife/centos-nginx-jdk8-mysql
WORKDIR /lsg/
COPY backend/target/*.jar ./app.jar
COPY entrypoint.sh ./
EXPOSE 9527