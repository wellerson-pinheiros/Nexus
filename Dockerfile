# ==========================================
# Etapa 1 - Build da aplicação
# ==========================================
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8


# ==========================================
# Etapa 2 - Imagem final
# ==========================================
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]