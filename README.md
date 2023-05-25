## Application Description
The application was developed using Java 13, Spring Boot 2.5, Maven, Amazon OpenSearch, MySQL 5.34, RabbitMQ, and Redis. The main purpose of the app is to provide the frontend with access to aggregated measurement data and to store user-defined configurations for packages and probes.

## Running the Application
To run the application, it's necessary to run all required dependent services. Use environment variables to configure the Docker images and Java application.

## Dependent Services
You can use Docker images locally to set up MySQL databases. You need at least one client database and one admin database.

To create a MySQL container, you can use the following command:

```
docker run -d \
--name mysql \
-e MYSQL_DATABASE=${MYSQL_DATABASE} \
-e MYSQL_USER=${MYSQL_USER} \
-e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
-e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
-p ${MYSQL_PORT}:3306 \
mysql:5.7.34`
```

Once the container is up, need to create the admin/tenant databases inside the running Docker container.

For RabbitMQ, you can use the following command to create a container:

```
docker run -d \
--name rabbitmq \
-p ${RABBIT_MQ_PORT}:5672 \
-p ${RABBIT_MQ_MANAGEMENT_PORT}:15672 \
rabbitmq:3-management
```

To set up Redis, use the following command:

```
docker run -d --name redis-stack-server -p ${REDIS_PORT}:6379 redis/redis-stack-server:latest
```

For OpenSearch use the following command:

```
docker run -d \
--name opensearch \
-p ${ELASTICSEARCH_PORT}:9200 \
opensearchproject/opensearch:1.3.0
```

To set up Logstash, use the following command:

```
docker run -d \
--name logstash \
-v /path/to/logstash.conf:/usr/share/logstash/pipeline/logstash.conf:ro \
-v /path/to/logs:/usr/share/logstash/logs \
-p ${LOGSTASH_PORT}:5044 \
docker.elastic.co/logstash/logstash:8.7.0
```

After running the Docker images, configure the application.yaml file.

# Microservices Architecture
The project has a microservice architecture:

#### Backend: 
https://gitlab.martes-specure.com/sah/backend

Base service (Backend) is the main service that communicates with the client via REST requests and with other services via RabbitMQ.
BS stores information in OpenSearch and MySQL. OpenSearch is used for storing measurements and calculating geo-shapes, while information about entities is stored in MySQL.
Additionally, MySQL is used as a redundant cold storage for measurements.

BS communicates with the scheduler to create triggers responsible for ping tests, delayed notifications, and company calendars.
BS should have contact with Logstash for logging.

Also, BS has contact with Redis. Redis stores caches of measurements and IP addresses.

#### Dataexport: 
https://gitlab.martes-specure.com/sah/dataexport

DataExport is a service for collecting statistics and exporting open data.
It communicates with clients via REST and with OpenSearch.
It contains several jobs for preparing reports and saving them to S3 as a cache-like mechanism.

#### Scheduler:
https://gitlab.martes-specure.com/sah/scheduler

Scheduler is a microservice built around the Quartz library.
Essentially, this microservice is responsible for creating and executing triggers.
It communicates exclusively via RabbitMQ with the Notification service and the main service.

#### Notification: 
https://gitlab.martes-specure.com/sah/notification

Notification service - the simplest service created for sending messages to the user.
#### Sah security:
https://gitlab.martes-specure.com/sah/sah-security

Library to cover security part of this application

You need to have installed KeyCloak. 

To compile Dataexport, Backend, and Notification services, you need to have the sah-security artifact compiled first.

Scheduler requires a MySQL database before running, and Notification requires a MySQL database before running. Scheduler is based on the QUARTZ library.

The Backend service contains regular unit tests and integration tests. You can configure whether to run integration tests or not by setting the value
of the integrationEnabled method in the AbstractIntegrationTest class to true or false.

In order to run integration tests, you need to have a running MySQL database, OpenSearch instance, and Redis instance.

### The MySQL database must have prepared data, such as:

##### Measurement server
##### Provider
##### Mobile Model
##### Package
##### Probe
##### Site

### The OpenSearch instance must have indices, such as:

##### Basic_test
##### Basic_qos_test

Additionally, You need to ensure that the Redis instance is running and configured properly to support the integration tests.
###
Run local integration test `mvn test -Dspring.profiles.active=integration-test`

## Swagger
There was configured a Swagger for each environment:
Local: http://localhost:8080/swagger-ui.html
Dev: http://api-dev.nettest.org/swagger-ui.html
Stage: http://api-stage.nettest.org/swagger-ui.html
Prod: https://api.nettest.org/swagger-ui.html

## CI
There was configured automatic deployment for branches: dev, stage and prod.