FROM openjdk:20
WORKDIR /opt
ENV PORT 80
EXPOSE 80
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar