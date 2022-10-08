FROM openjdk:11
WORKDIR /TimeCapture
COPY TimeCapture-0.0.1-SNAPSHOT.jar TimeCapture.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "TimeCapture.jar"]