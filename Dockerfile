FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/challenge-blue-orange-api-1.0.0-SNAPSHOT.jar

VOLUME /tmp
EXPOSE 8080/tcp
ADD ${JAR_FILE} app.jar

HEALTHCHECK --interval=1m --timeout=3s CMD curl -f http://localhost:8080/health || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]