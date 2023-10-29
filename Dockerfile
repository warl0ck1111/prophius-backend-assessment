FROM openjdk:oracle

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} prophius-assessment.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/prophius-assessment.jar"]

