FROM waterknife/centos-nginx-jdk8
WORKDIR /lsg/
COPY backend/exam/target/*.jar ./app.jar
COPY entrypoint.sh ./
EXPOSE 80
ENTRYPOINT ["sh","entrypoint.sh"]
