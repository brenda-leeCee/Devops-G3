FROM openjdk:latest
COPY ./target/devops.jar.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops.jar.jar"]
