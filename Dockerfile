FROM adoptopenjdk/maven-openjdk8 AS builder
MAINTAINER Priyank Garg
WORKDIR /app
COPY . /app
RUN mvn clean install

FROM adoptopenjdk/openjdk8:ubi-jre
COPY --from=builder /app/target/*.jar deployment.jar
USER 1001
ENTRYPOINT java $JVM_OPTS -jar deployment.jar