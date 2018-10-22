FROM gradle:4.10-jdk8-slim
LABEL maintainer="luk.zim91@gmail.com"

COPY --chown=gradle:gradle . /opt/srv
WORKDIR /opt/srv
RUN gradle clean --no-daemon && gradle build --no-daemon
ENTRYPOINT [ "gradle", "bootRun", "--no-daemon" ]

