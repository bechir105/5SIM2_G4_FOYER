FROM openjdk:17-jdk-alpine

# Expose le port de l'application Spring Boot
EXPOSE 8089
# Ajoute le livrable Spring Boot dans l'image
ADD target/Foyer-0.0.1-20241109.144150-1.jar Foyer-0.0.1-20241109.144150-1.jar

# Commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/Foyer-0.0.1-20241109.144150-1.jar"]
