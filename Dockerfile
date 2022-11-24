FROM openjdk:8-jre-alpine
LABEL maintainer="2578908933@qq.com"
#复制打好的jar包
WORKDIR /qrot
COPY cache cache
COPY target/*.jar app.jar
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
touch app.jar;

ENV JAVA_OPTS=""
ENV PARAMS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar app.jar $PARAMS" ]