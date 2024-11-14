FROM openjdk:latest
COPY ./target/airelux.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "airelux.jar", "world:3306", "10000"]
