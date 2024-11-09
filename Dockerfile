FROM openjdk:17
WORKDIR /app
EXPOSE 8089
ADD target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]