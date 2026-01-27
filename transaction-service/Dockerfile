FROM maven:3.8.6-openjdk-8 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM payara/micro:5.2022.2-jdk8
COPY --from=build /app/target/transaction-service.war /opt/payara/deployments/ROOT.war