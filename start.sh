# !/bin/bash

/usr/local/bin/filebeat -e -c /etc/filebeat/filebeat.yml &
java -jar /app.jar --server.port=80