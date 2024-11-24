## Build and Start the Project Using Docker

#### Prerequisites
Install the following technology:

| Technology | URL to Download                  |
|------------|----------------------------------|
| Docker     | https://docs.docker.com/install/ |

#### 1. Preparation of Configuration Files
To build and run the project in docker it is necessary to prepare configuration file.

* Fill settings in the [answers-storage.properties](https://github.com/cyberrangecz/backend-answers-storage/blob/master/etc/answers-storage.properties) file and save it.

[//]: # (TODO update deployment info)
#### 2. Build Docker Image
In the project root folder (folder with Dockerfile), run the following command:
```shell
$ sudo docker build \
  --build-arg PROPRIETARY_REPO_URL=https://gitlab.ics.muni.cz/api/v4/projects/2358/packages/maven \
  -t answers-storage-image \
  .
```

Dockefile contains several default arguments:
* USERNAME=postgres - the name of the user to connect to the database. 
* PASSWORD=postgres - user password.
* POSTGRES_DB=answers-storage - the name of the created database.
* PROJECT_ARTIFACT_ID=answers-storage - the name of the project artifact.
* PROPRIETARY_REPO_URL=YOUR-PATH-TO-PROPRIETARY_REPO.

Those arguments can be overwritten during the build of the image, by adding the following option for each argument: 
```bash
--build-arg {name of argument}={value of argument} 
``` 

#### 3. Start the Project
Start the project by running docker container. Instead of usage of the PostgreSQL database, you can use the in-memory database H2. It just depends on the provided configuration. To run a docker container, run the following command: 
```shell
$  sudo docker run \
   --name answers-storage-container -it \
   --network host \
   -p 8087:8087 \
   answers-storage-image
```

Add the following option to use the custom property file:
```shell
-v {path to your config file}:/app/etc/answers-storage.properties
```

To create a backup for your database add the following docker option:
```shell
-v db_data_training:/var/lib/postgresql/11/main/
```
