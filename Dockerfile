#
# Build stage
#
FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

ENV PORT 8080

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/spring-security-demo-0.0.1-SNAPSHOT.jar /usr/local/lib/webapp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/webapp.jar"]
