FROM openjdk
VOLUME /tmp
ADD target/*.jar /app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar", "--server.port=80" ]
