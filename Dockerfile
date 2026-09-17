#Dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY ./target/*.jar /app/holidayDessert-0.0.1-SNAPSHOT.jar
WORKDIR /app
RUN sh -c 'touch holidayDessert-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","holidayDessert-0.0.1-SNAPSHOT.jar"]