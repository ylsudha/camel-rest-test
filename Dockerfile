FROM openjdk:11
ADD target/camel-rs-docker.jar camel-rs-docker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "camel-rs-docker.jar"]