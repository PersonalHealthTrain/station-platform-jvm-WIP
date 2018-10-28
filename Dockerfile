FROM gradle:4.10-jdk8
LABEL maintainer="luk.zim91@gmail.com"

USER root
COPY . /opt/srv
WORKDIR /opt/srv
RUN gradle clean build --no-daemon && chmod +x /opt/srv/docker-entrypoint.sh

ENTRYPOINT [ "/opt/srv/docker-entrypoint.sh" ]
