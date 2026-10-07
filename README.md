## CyberRangeCZ Platform Answers Storage

Backend service storing the variant answers of sandboxes. It is built with Java 25 and Spring Boot 4.1.1
and listens on port 8087 under the context path `/answers-storage/api/v1`. The service has no
authentication; it is meant to run behind a reverse proxy that hides it from outside the cluster.

## Build and Start the Project Using Docker

#### Prerequisites
Install the following technology:

| Technology | URL to Download                  |
|------------|----------------------------------|
| Docker     | https://docs.docker.com/install/ |

#### 1. Preparation of Configuration Files
To build and run the project in docker it is necessary to prepare configuration file.

* Fill settings in the [answers-storage.properties](https://github.com/cyberrangecz/backend-answers-storage/blob/master/etc/answers-storage.properties) file and save it.

The shipped file points at PostgreSQL; replace the `{...}` placeholders of the `spring.datasource.*`
properties with the connection of your database. Flyway creates the schema on startup. The container runs
as a non-root user and reports its health through `/answers-storage/api/v1/actuator/health`.

#### 2. Build Docker Image
In the project root folder (folder with Dockerfile), run the following command:
```shell
$ sudo docker build -t answers-storage-image .
```

The Dockerfile contains the following arguments:
* PROJECT_ARTIFACT_ID=answers-storage - the name of the project artifact.
* MAVEN_CLI_OPTS= - extra options appended to the Maven command that builds the jar.

Those arguments can be overwritten during the build of the image, by adding the following option for each argument:
```bash
--build-arg {name of argument}={value of argument}
```

`DockerfileStatic` builds an image from a jar built beforehand, given by the `JAR_FILE` build argument; it
does not copy the configuration file, so mount it as described below:
```shell
$ ./mvnw -ntp clean package -DskipTests -DskipChecks=true
$ sudo docker build -f DockerfileStatic --build-arg JAR_FILE=target/answers-storage-2.0.0.jar -t answers-storage-image .
```

#### 3. Start the Project
Start the project by running docker container. To run a docker container, run the following command:
```shell
$ sudo docker run \
   --name answers-storage-container -it \
   -p 8087:8087 \
   answers-storage-image
```

Add the following option to use the custom property file:
```shell
-v {path to your config file}:/app/etc/answers-storage.properties
```

The API documentation is served at `http://localhost:8087/answers-storage/api/v1/swagger-ui/index.html`.

## Build Without Docker

Building needs JDK 25; the Maven wrapper downloads Maven itself.
```shell
$ ./mvnw clean package -DskipChecks=true
```
The `-DskipChecks=true` option skips the formatting, style, SpotBugs and dependency checks; without it the
OWASP dependency check needs the `NVD_API_TOKEN` and `NVD_DATA_DIR` environment variables.
