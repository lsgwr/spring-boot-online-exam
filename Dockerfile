FROM waterknife/centos-nginx-jdk8-mysql
WORKDIR /lsg/
COPY backend/target/*.jar ./app.jar
COPY entrypoint.sh ./
RUN chmod 777 entrypoint.sh
EXPOSE 9527