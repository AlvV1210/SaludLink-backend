# =========================================================
#  Stage 1: build — compila el JAR con Maven y JDK 21
# =========================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# =========================================================
#  Stage 2: runtime — JRE 21, imagen liviana
# =========================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /workspace/target/saludlink-0.0.1-SNAPSHOT.jar app.jar

# Render asigna el puerto en PORT en runtime.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
