# This spring application example connects to IBM Databases for MongoDB and IBM Event Stream (Kafka).

- [How to start the application](#how-to-start-the-application)
- [How to build docker image](#how-to-build-docker-image)

## How to start the application

Set the following environment variables to connect to IBM Databases for MongoDB.

```bash
MONGO_DB_URL=<database_url starting with mongodb://>
MONGO_DB_NAME=<database_name>
```

Download the IBM Databases for MongoDB certificate, generate the JKS and import the certificate in JKS.

```bash
keytool -genkey -alias <fully_qualified_domain_name> -keyalg RSA -keystore <key_store_name>.jks -keysize 2048
keytool -importcert -trustcacerts -file <certificate_file_full_path> -keystore <key_store_name>.jks -storepass <key_store_password> -alias <certificate_specific_unique_name>
```

Build `.jar` file and start the application by passing JVM parameters as in below.

```bash
mvn clean install
java -Djavax.net.ssl.trustStore=<key_store_full_path>.jks -Djavax.net.ssl.trustStorePassword=<key_store_password> -jar <generated_jar_file_full_path>.jar
```

## How to build docker image
```bash
docker -t <registry_url>/<repo_name>/<image_name>:<image_version> build .
```
