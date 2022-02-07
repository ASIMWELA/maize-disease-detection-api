FROM openjdk:8
ARG JAR_FILE=target/Maize-Disease-Detection-api.jar
COPY ${JAR_FILE} Maize-Disease-Detection-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Maize-Disease-Detection-api.jar"]