FROM waterknife/centos-nginx-jdk8-mysql
WORKDIR /lsg/
COPY backend/target/*.jar ./app.jar
EXPOSE 9527