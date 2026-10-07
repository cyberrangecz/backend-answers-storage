ARG PROJECT_ARTIFACT_ID=answers-storage

############ BUILD STAGE ############
FROM maven:3.10.0-eclipse-temurin-25 AS build
WORKDIR /app

ARG PROJECT_ARTIFACT_ID
ARG MAVEN_CLI_OPTS=

COPY pom.xml /app/pom.xml
COPY src /app/src

# Build JAR file
RUN mvn -ntp -q clean install -DskipTests -DskipChecks=true $MAVEN_CLI_OPTS && \
    cp /app/target/$PROJECT_ARTIFACT_ID-*.jar /app/$PROJECT_ARTIFACT_ID.jar

############ RUNNABLE STAGE ############
FROM eclipse-temurin:25-jre-noble AS runnable

WORKDIR /app

ARG PROJECT_ARTIFACT_ID

ENV PROJECT_ARTIFACT_ID=${PROJECT_ARTIFACT_ID}

COPY etc/$PROJECT_ARTIFACT_ID.properties /app/etc/$PROJECT_ARTIFACT_ID.properties
COPY entrypoint.sh /app/entrypoint.sh
COPY --from=build /app/$PROJECT_ARTIFACT_ID.jar ./

RUN apt-get update && \
    # nc is used by the wait for it function, see entrypoint.sh; curl by the HEALTHCHECK
    apt-get install -y netcat-openbsd curl && \
    rm -rf /var/lib/apt/lists/* && \
    chmod a+x entrypoint.sh

EXPOSE 8087

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl --fail --silent http://localhost:8087/answers-storage/api/v1/actuator/health || exit 1

ENTRYPOINT ["./entrypoint.sh"]
