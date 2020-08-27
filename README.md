# This spring boot application example connects to IBM Databases for MongoDB and IBM Event Streams (Kafka).

- [How to start the application](#how-to-start-the-application)
- [How to build docker image](#how-to-build-the-docker-image)

## How to start the application

1. Set the following environment variables to connect to IBM Databases for MongoDB.

```bash
export MONGO_DB_URL=<database_url starting with mongodb://>
export MONGO_DB_NAME=<database_name>
```

2. Download the certificate from IBM Databases for MongoDB instance dashboard, generate the JKS or use JDK cacerts and import the MongoDB certificate in JKS.

```bash
keytool -genkey -alias <fully_qualified_domain_name> -keyalg RSA -keystore <key_store_name>.jks -keysize 2048
or
cp <JDK_Cert_Path>/cacerts <key_store_name>.jks     # Use the existing JDK cacerts as JKS.
keytool -storepasswd -keystore keystore.jks     # change the JKS password.
keytool -importcert -trustcacerts -file <certificate_file_full_path> -keystore <key_store_name>.jks -storepass <key_store_password> -alias <certificate_specific_unique_name>
```

3. From the Event Streams for IBM Cloud instance dashboard, click Service Credentials and select or create a new one.

4. Copy the above created credentials content and set the following environment variable to connect to IBM Event Streams (Kafka).

```bash
export ES_KAFKA_SERVICE='{
  "api_key": "...",
  "apikey": "...",
  "iam_apikey_description": "...",
  "iam_apikey_name": "...",
  "iam_role_crn": "...",
  "iam_serviceid_crn": "...",
  "instance_id": "...",
  "kafka_admin_url": "...",
  "kafka_brokers_sasl": [
    "...",
    "...",
  ],
  "kafka_http_url": "...",
  "password": "...",
  "user": "..."
}'
```

5. Install event-stream cli and initialize it to connect to event stream instance.

```bash
ibmcloud plugin install event-streams
ibmcloud es init
```

6. Build `.jar` file and start the application by passing JVM parameters as in below.

```bash
mvn clean install
java -Djavax.net.ssl.trustStore=<key_store_full_path>.jks -Djavax.net.ssl.trustStorePassword=<key_store_password> -jar <generated_jar_file_full_path>.jar
```

## How to build and push the docker image
```bash
docker -t <registry_url>/<repo_name>/<image_name>:<image_version> build .
docker push <registry_url>/<repo_name>/<image_name>:<image_version>
```
