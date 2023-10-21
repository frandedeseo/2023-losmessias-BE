# Usa una imagen de Maven como base para construir la aplicación
FROM maven:3.8.4-openjdk-17-slim AS build
# Establece el directorio de trabajo en /app
WORKDIR /app
# Copia el archivo POM y los recursos necesarios
COPY pom.xml .
COPY src ./src
# Empaqueta la aplicación usando Maven
RUN mvn clean package
# Usa una imagen de Java 17 como base para ejecutar la aplicación
FROM openjdk:17-jdk-slim
# Establece el directorio de trabajo en /app
WORKDIR /app
# Copia el archivo JAR de la etapa de construcción anterior al contenedor
COPY --from=build /app/target/leherer-0.0.1-SNAPSHOT.jar /app/app.jar
# Expone el puerto 8080 para que la aplicación sea accesible
EXPOSE 8080
# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]