# Usa una imagen de Java 17 como base
FROM openjdk:17-jdk-slim
# Establece el directorio de trabajo en /app
WORKDIR /app
# Copia el archivo JAR de la aplicación a la imagen
COPY target/leherer-0.0.1-SNAPSHOT.jar /app/app.jar
# Copia las dependencias de Maven al contenedor
COPY ~/.m2/repository /root/.m2/repository
# Expone el puerto 8080 para que la aplicación sea accesible
EXPOSE 8080
# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]
