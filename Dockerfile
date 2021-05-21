FROM waterknife/centos-nginx-jdk8
WORKDIR /lsg/
COPY backend/exam/target/*.jar ./app.jar
COPY doc/deploy/nginx.conf /etc/nginx/nginx.conf
COPY frontend/exam/dist /usr/share/nginx/html
COPY entrypoint.sh ./
EXPOSE 80 9527
ENTRYPOINT ["sh","entrypoint.sh"]
