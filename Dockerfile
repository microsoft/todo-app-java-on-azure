FROM ubuntu:16.04

RUN echo "Install filebeat"

ENV FILEBEAT_VERSION 5.4.1

RUN apt-get update && \
    apt-get -y install wget && \
    wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz && \
    echo "$(wget -qO - https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz.sha1) filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz" | sha1sum -c - && \
    tar xzf filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz && \
    mv filebeat-${FILEBEAT_VERSION}-linux-x86_64/filebeat /usr/local/bin && \
    rm -rf /filebeat* && \
    apt-get -y remove wget && \
    apt-get -y autoremove

RUN apt-get -y install curl

COPY filebeat.yml /etc/filebeat/

CMD ["/usr/local/bin/filebeat", "-e", "-c", "/etc/filebeat/filebeat.yml", "&"]

RUN echo "Install Java"

RUN apt-get update \
  && apt-get -y --force-yes install software-properties-common python-software-properties debconf-utils

RUN add-apt-repository -y ppa:openjdk-r/ppa \
  && apt-get update \
  && apt-get install -y openjdk-8-jre

RUN echo "Run java application"

VOLUME /tmp
ADD target/*.jar /app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar", "--server.port=80" ]