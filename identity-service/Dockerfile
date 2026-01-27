# Build Stage
FROM maven:3.8.6-openjdk-8 as build
WORKDIR /app
COPY pom.xml .
# Baixa dependências para cache
RUN mvn dependency:go-offline 
COPY src ./src
RUN mvn package -DskipTests

# Run Stage (Payara Micro suporta Java 8 e Jakarta EE 8)
FROM payara/micro:5.2022.2-jdk8
COPY --from=build /app/target/identity-service.war /opt/payara/deployments/ROOT.war
# Configurações de JVM se necessário
ENV JAVA_OPTS="-Xmx512m"