FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE
EXPOSE 9000
COPY *.jar phonebook-api.jar
ENTRYPOINT ["java","-jar","/phonebook-api.jar"]