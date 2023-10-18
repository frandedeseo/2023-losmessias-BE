# Usa una imagen de Java como base
FROM openjdk:17
# Establece el directorio de trabajo en /app
WORKDIR /app
# Copia el archivo JAR de la aplicación a la imagen
COPY target/leherer-0.0.1-SNAPSHOT.jar /app/app.jar
# Instala las dependencias de MySQL
RUN apt-get update && apt-get install -y mysql-client
# Expone el puerto 8080 para que la aplicación pueda ser accesible
EXPOSE 8080
# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]