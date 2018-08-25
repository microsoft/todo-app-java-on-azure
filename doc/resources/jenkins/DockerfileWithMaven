FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN

ARG dbUrl
ARG dbKey
ARG dbName

ENV DOCUMENTDB_URI=${dbUrl}
ENV DOCUMENTDB_KEY=${dbKey}
ENV DOCUMENTDB_DBNAME=${dbName}

COPY pom.xml /tmp/
COPY checkstyle.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

RUN cp /tmp/target/*.jar /app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar", "--server.port=80" ]
