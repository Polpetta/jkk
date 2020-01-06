FROM gradle:6.0-jdk11 as builder

WORKDIR /opt/jkk/
COPY . /opt/jkk/
RUN gradle installDist

FROM gcr.io/distroless/java:11-debug

COPY --from=builder /opt/jkk/build/install/ /opt/
ENTRYPOINT ["sh", "/opt/jkk/bin/jkk"]