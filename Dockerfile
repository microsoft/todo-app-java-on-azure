FROM ubuntu:16.04

RUN apt update
RUN apt install -y openssh-server curl libc++1 cifs-utils
RUN apt install -y libssh2-1 libunwind8 libib-util
RUN apt install -y lttng-tools lttng-modules-dkms liblttng-ust0 chrpath members sshpass nodejs nodejs-legacy npm locales
RUN apt install -y cgroup-bin acl net-tools apt-transport-https rssh vim atop libcurl3 openjdk-8-jre
RUN sh -c 'echo "deb [arch=amd64] https://apt-mo.trafficmanager.net/repos/dotnet-release/ xenial main" > /etc/apt/sources.list.d/dotnetdev.list'
RUN apt-key adv --keyserver apt-mo.trafficmanager.net --recv-keys 417A0893
RUN apt-get update --fix-missing
RUN apt-get install -y dotnet-runtime-2.0.7
#/etc/init.d/ssh start
RUN locale-gen en_US.UTF-8
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

COPY target/AppPkg /Code
COPY entryPoint.sh /Code
COPY start_lttng.sh /Code
WORKDIR /Code
ENTRYPOINT ["./entryPoint.sh"]