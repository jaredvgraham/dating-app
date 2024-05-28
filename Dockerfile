FROM openjdk:17-jdk-slim
VOLUME /tmp

# Copy the wait-for-it script into the container
COPY wait-for-it.sh /wait-for-it.sh

# Make the wait-for-it script executable
RUN chmod +x /wait-for-it.sh

# Copy the JAR file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} datebyrate.jar

# Use wait-for-it.sh to wait for the database to be ready, then start the application
ENTRYPOINT ["./wait-for-it.sh", "db:5432", "--", "java", "-jar", "/datebyrate.jar"]

