FROM gradle:6.0-jdk11 as builder

WORKDIR /opt/jkk/
COPY . /opt/jkk/
RUN gradle standalone

FROM gcr.io/distroless/java:11

COPY --from=builder /opt/jkk/build/libs/*.jar /opt/jkk/jkk.jar
ENTRYPOINT ["java", "--add-opens=java.base/java.lang=ALL-UNNAMED", "-jar", "/opt/jkk/jkk.jar"]
