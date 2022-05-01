FROM openjdk:17-alpine

#pass version as a param
COPY build/libs/transactions-1.0.jar transaction-app.jar
EXPOSE 8080
CMD ["java","-jar","transaction-app.jar"]