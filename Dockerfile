# Utiliser une image officielle OpenJDK 17 comme image de base
FROM openjdk:17-jdk-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Installer Maven
RUN apk add --no-cache maven

# Copier les fichiers de projet dans le conteneur
COPY pom.xml .
COPY src/ src/

# Exécuter la commande Maven pour construire et empaqueter l'application
RUN mvn clean package -DskipTests=true

# Copier le fichier JAR résultant dans le conteneur
COPY target/*.jar /app/app.jar

# Créer un groupe et un utilisateur pour exécuter l'application
RUN addgroup -S spring && adduser -S spring -G spring

# Définir le groupe et l'utilisateur à utiliser lors de l'exécution du conteneur
USER spring:spring

# Définir la commande d'entrée pour exécuter l'application
CMD ["java", "-jar", "/app/app.jar"]
