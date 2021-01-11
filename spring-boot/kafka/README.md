# Kafka-Client examples with OAuth2 & AVRO-Schema-Registry
## Description
Basic Kafka-Producer&Consumer that features
* OAUth2 Authentication/Authorization
* AVRO-Schema with APICURIO backed schema-registry
* spring-boot
 

## Prerequisites
Keycloak has to be configured for authenticating/authorizing Kafka-Clients (https://github.com/strimzi/strimzi-kafka-oauth/blob/master/examples/README-authz.md)

## Prepare Keycloak
We'll be using the realm-roles from the example (https://github.com/strimzi/strimzi-kafka-oauth/blob/master/examples/README-authz.md). 

* Create confidential client **avro-producer** 
* Add realm-role DEV-TEAM-A to the service-account-roles of  **avro-producer**
* Create confidential client **avro-consumer** 
* Add realm-role DEV-TEAM-A to the service-account-roles of  **avro-consumer**
* Select the keycloak-client that represents your kafka-cluster (kafka in the example)

### Create Resources
Name: Topic:avro-sample
Type: Topic

Name: Group:avro-sample
Type: Group

### Create Permissions
For each Resource create a permission, e.g.

Name: Dev Team A has full access to Avro-Sample Topic
Resource: Topic:avro-sample 
Policy: Dev Team A
Strategy: Unanimous

## Schema-Registry
Both producer and consumer require that the schema is present in a schema-registry. For better control of the schema-versions the schema is not updated automatically in the schema-registry by the producer.

## Running the producer
### Uploading/Updating the schema
There is a maven-goal for updating the schema. 

```
REGISTRY_URL=https://fqdn/api mvn -Pupload generate-sources
```

If the registry is secured (with basic auth) you can pass auth-info with

```
REGISTRY_URL=https://user:password@fqdn/api mvn -Pupload generate-sources
```

### Running the producer
The producer can be configured by setting the following env-variables

| *Variable* | *Description* |
| --- | --- |
| `REGISTRY_URL` | URL to Apicurio API, eg. https://fqdn/api. You can pass basic-auth with https://user:pass@fqdn/api|
| `OAUTH_TOKEN_ENDPOINT_URI` | Keycloak token-endpoint, e.g. https://fqdn/auth/realms/REALM/protocol/openid-connect/token |
| `OAUTH_CLIENT_ID` | Producer client, e.g. avro-producer |
| `OAUTH_CLIENT_SECRET` | Client-secret |
| `OAUTH_USERNAME_CLAIM` | preferred_username |
| `BOOTSTRAP_SERVER` | Kafka-Bootstrap Server/Port |

Start it like that
```
REGISTRY_URL=... OAUTH_TOKEN_ENDPOINT_URI=... OAUTH_CLIENT_ID=... OAUTH_CLIENT_SECRET=... OAUTH_USERNAME_CLAIM=preferred_username BOOTSTRAP_SERVER=... mvn spring-boot:run
```


If all is well it will produce messages every 5 seconds

```
21-01-11 12:19:26.668  INFO 29637 --- [   scheduling-1] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.6.0
2021-01-11 12:19:26.668  INFO 29637 --- [   scheduling-1] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 62abe01bee039651
2021-01-11 12:19:26.670  INFO 29637 --- [   scheduling-1] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1610363966668
2021-01-11 12:19:26.778  INFO 29637 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: W22XC5yNQZO3o5nyBnt9Kg
2021-01-11 12:19:27.088  INFO 29637 --- [   scheduling-1] s.i.s.service.UserProducer               : Produced user -> {"name": "peter", "age": 37, "amount": 5049, "anothervalue": 1}
2021-01-11 12:19:31.544  INFO 29637 --- [   scheduling-1] s.i.s.service.UserProducer               : Produced user -> {"name": "paul", "age": 19, "amount": 526, "anothervalue": 1}
2021-01-11 12:19:36.548  INFO 29637 --- [   scheduling-1] s.i.s.service.UserProducer               : Produced user -> {"name": "mary", "age": 64, "amount": 8068, "anothervalue": 1}
```

### Pitfalls
* No permissions for creating topics,writing to topics. Check Keycloak
* Schema not present in registry

## Running the consumer
### Get the current schema
There is a maven-goal for downloading the latest schema. 

```
REGISTRY_URL=https://fqdn/api mvn -Pdownload-schema generate-sources
```

If the registry is secured (with basic auth) you can pass auth-info with

```
REGISTRY_URL=https://user:password@fqdn/api mvn -Pdownload-schema generate-sources
```

### Running the producer
The consumer can be configured by setting the following env-variables

| *Variable* | *Description* |
| --- | --- |
| `REGISTRY_URL` | URL to Apicurio API, eg. https://fqdn/api. You can pass basic-auth with https://user:pass@fqdn/api|
| `OAUTH_TOKEN_ENDPOINT_URI` | Keycloak token-endpoint, e.g. https://fqdn/auth/realms/REALM/protocol/openid-connect/token |
| `OAUTH_CLIENT_ID` | Producer client, e.g. avro-consumer |
| `OAUTH_CLIENT_SECRET` | Client-secret |
| `OAUTH_USERNAME_CLAIM` | preferred_username |
| `BOOTSTRAP_SERVER` | Kafka-Bootstrap Server/Port |

Start it like that
```
REGISTRY_URL=... OAUTH_TOKEN_ENDPOINT_URI=... OAUTH_CLIENT_ID=... OAUTH_CLIENT_SECRET=... OAUTH_USERNAME_CLAIM=preferred_username BOOTSTRAP_SERVER=... mvn spring-boot:run
```


If all is well it will consumer the messages present in the topic

```
2021-01-11 12:25:07.740  INFO 30072 --- [ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : avro-sample: partitions assigned: [avro-sample-1, avro-sample-2, avro-sample-0, avro-sample-3]
2021-01-11 12:25:08.343  INFO 30072 --- [ntainer#0-0-C-1] s.i.s.service.UserConsumer               : Consumed message -> {"name": "hans", "age": 26, "amount": 4138, "anothervalue": 1}
2021-01-11 12:25:08.343  INFO 30072 --- [ntainer#0-0-C-1] s.i.s.service.UserConsumer               : Consumed message -> {"name": "hans", "age": 91, "amount": 1205, "anothervalue": 1}
2021-01-11 12:25:08.343  INFO 30072 --- [ntainer#0-0-C-1] s.i.s.service.UserConsumer               : Consumed message -> {"name": "hans", "age": 62, "amount": 4722, "anothervalue": 1}
```

### Pitfalls
* No permissions for reading topics/groups. Check Keycloak
* Schema not present in registry



