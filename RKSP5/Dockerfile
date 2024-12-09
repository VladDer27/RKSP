FROM amazoncorretto:20
WORKDIR /app
COPY target/RKSP5-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
CMD ["java", "-jar", "app.jar"]