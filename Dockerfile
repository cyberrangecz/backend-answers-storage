############ BUILD STAGE ############
FROM maven:3.6.2-jdk-11-slim AS build
WORKDIR /app
ARG PROJECT_ARTIFACT_ID=kypo-answers-storage
# Default link to proprietary repository, e.g., GitLab repository
ARG PROPRIETARY_REPO_URL=YOUR-PATH-TO-PROPRIETARY_REPO
COPY pom.xml /app/pom.xml
COPY src /app/src
# Build JAR file
RUN mvn clean install -DskipTests -Dproprietary-repo-url=$PROPRIETARY_REPO_URL && \
    cp /app/target/$PROJECT_ARTIFACT_ID-*.jar /app/kypo-answers-storage.jar

############ RUNNABLE STAGE ############
FROM openjdk:11-jre-slim AS runnable
WORKDIR /app
COPY /etc/kypo-answers-storage.properties /app/etc/
COPY entrypoint.sh /app/entrypoint.sh
COPY --from=build /app/kypo-answers-storage.jar ./
RUN apt-get update && \
    # Required to use nc command in the wait for it function, see entrypoint.sh
    apt-get install -y netcat && \
    # Make a file executable
    chmod a+x entrypoint.sh
EXPOSE 8087
ENTRYPOINT ["./entrypoint.sh"]
