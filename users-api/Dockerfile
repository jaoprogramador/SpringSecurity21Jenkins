# --------------------------------------------------------------------------------
# Etapa 1: COMPILACIÓN (Opcional: Si construyes dentro de Docker)
# --------------------------------------------------------------------------------
# Si ya tienes el JAR compilado localmente, puedes OMITIR esta etapa y 
# simplemente copiar el JAR directamente en la Etapa 2.
#
# FROM maven:3.9.5-amazoncorretto-21 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests
#
# --------------------------------------------------------------------------------
# Etapa 2: EJECUCIÓN (Runtime) - Imagen final y pequeña
# --------------------------------------------------------------------------------

# Usa una imagen ligera con Java 21 JRE, como Eclipse Temurin o Amazon Corretto
# 'jre-jammy' es la versión mínima de solo ejecución.
FROM eclipse-temurin:21-jre-jammy AS final

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Define el puerto que expone Spring Boot (por defecto 8080)
EXPOSE 8080

# Copia el JAR compilado desde tu máquina local (el contexto de la construcción)
# usando el nombre exacto que tiene en la carpeta target/
# Origen: target/users-api-0.0.1-SNAPSHOT.jar
# Destino: users-api.jar (dentro del contenedor)
COPY target/users-api-0.0.1-SNAPSHOT.jar users-api.jar

# Define el punto de entrada para ejecutar la aplicación JAR
ENTRYPOINT ["java", "-jar", "users-api.jar"]