FROM waterknife/centos-nginx-jdk8
WORKDIR /lsg/
COPY target/exam-0.0.1-SNAPSHOT.jar ./
COPY nginx.conf /usr/local/nginx/conf/
COPY dist /usr/local/nginx/html
EXPOSE 80 9527
CMD '/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf && nohup java -jar exam-0.0.1-SNAPSHOT.jar'