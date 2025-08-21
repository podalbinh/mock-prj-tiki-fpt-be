FROM openjdk:17-jdk-slim
WORKDIR /mock-prj-tiki-fpt-be
COPY target/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","backend-0.0.1-SNAPSHOT.jar"]
