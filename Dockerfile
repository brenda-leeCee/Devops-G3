FROM openjdk:latest
COPY ./target/airelux-1.0.0.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "airelux-1.0.0.jar", "world:3306", "10000"]
