FROM openjdk:17-jdk-alpine

# Expose le port de l'application Spring Boot
EXPOSE 8089
# Ajoute le livrable Spring Boot dans l'image
ADD target/Foyer-1.0.0.jar Foyer-1.0.0.jar

# Commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/Foyer-1.0.0.jar"]
