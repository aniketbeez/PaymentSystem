FROM amazoncorretto:17-alpine-jdk
ADD target/*.jar payment-risk-engine.jar
ENTRYPOINT ["java", "-jar", "payment-risk-engine.jar"]
