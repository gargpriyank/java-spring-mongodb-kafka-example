FROM adoptopenjdk/openjdk8:ubi-jre
MAINTAINER Priyank Garg
WORKDIR /app
COPY target/*.jar deployment.jar
USER 1001
ENTRYPOINT java $JVM_OPTS -jar deployment.jar