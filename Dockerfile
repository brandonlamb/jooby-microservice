FROM adoptopenjdk/openjdk8-openj9:jdk8u162-b12_openj9-0.8.0

RUN apt-get update && \
    apt-get dist-upgrade -y && \
    mkdir -p /opt/shareclasses && \
    mkdir -p /opt/app

COPY target/app.jar /opt/app
COPY run.sh /opt/app

EXPOSE 8080

#CMD ["/opt/app/run.sh"]
ENTRYPOINT ["/opt/app/run.sh"]
