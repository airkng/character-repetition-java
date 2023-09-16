FROM adoptopenjdk/openjdk15:ubi
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]