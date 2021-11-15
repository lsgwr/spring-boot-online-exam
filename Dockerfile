FROM waterknife/centos-nginx-jdk8-mysql
WORKDIR /lsg/
COPY backend/target/*.jar ./app.jar
COPY entrypoint.sh ./
RUN /usr/sbin/init
EXPOSE 80
ENTRYPOINT ["sh","entrypoint.sh"]
