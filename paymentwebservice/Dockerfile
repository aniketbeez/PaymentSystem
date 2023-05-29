FROM amazoncorretto:17-alpine-jdk
ADD target/*.jar payment-web-api.jar
ENTRYPOINT ["java", "-jar", "payment-web-api.jar"]
