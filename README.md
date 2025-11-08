[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-ssl-client-server/actions/workflows/maven-jvm-non-native-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-ssl-client-server/actions/workflows/maven-jvm-non-native-build.yml)
---

### ** Spring Boot SSL using PKCS12 JKS and PEM format **

---

# Getting Started

## Requirements:

```
Git: 2.51+
Spring Boot: 3.5.7
Maven: 3.9+
Java: 25
Docker Desktop(Optional): Tested on 4.47.0
```

## (Optional)Generate PKCS/JKS/PEM files

Required files are already in class path. If you want to generate your own self-signed then use below commands.
generate `PKCS12` file

```bash
keytool -genkeypair -alias springbootssl -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springbootssl.p12 -validity 36500
```

generate `JKS` file from PKCS12

```bash
keytool -importkeystore -srckeystore springbootssl.p12 -srcstoretype pkcs12 -destkeystore springbootssl.jks -deststoretype jks
```

generate private key and certificate file

```bash
openssl req -x509 -subj "/CN=localhost" -keyout private.key -out certificate.crt -sha256 -days 36500 -nodes -newkey rsa
```

for windows OS

```bash
openssl req -x509 -subj "//CN=localhost" -keyout private.key -out certificate.crt -sha256 -days 36500 -nodes -newkey rsa
```

## Clone this repository:

```bash
git clone https://github.com/deepaksorthiya/spring-boot-ssl-client-server.git
cd spring-boot-ssl-client-server
```

## Pre-requisite Configuration

check file [application.properties](src/main/resources/application.properties). this file contains PKCS12 and PEM based
ssl config.

## Build Project and Run Test Cases:

```bash
./mvnw clean package
```

### Run Project:

```bash
./mvnw spring-boot:run
```

## (Optional)Run Using Docker(docker should be running)

### Build Docker Image

```bash
./mvnw clean spring-boot:build-image -DskipTests
```

OR

```bash
docker build -t deepaksorthiya/spring-boot-ssl-client-server:0.0.1-SNAPSHOT . 
```

### Run Docker Image

```bash
docker run --name spring-boot-ssl-client-server -p 8080:8080 deepaksorthiya/spring-boot-ssl-client-server:0.0.1-SNAPSHOT
```

## Testing

```bash
curl -v -k https://localhost:8443/server-call
```

```bash
curl -v -k https://localhost:8443/client-call
```

## Reference Documentation

For further reference, please consider the following sections:

* [SSL Spring Boot](https://spring.io/blog/2023/11/07/ssl-hot-reload-in-spring-boot-3-2-0)
* [SSL Demo](https://spring.io/blog/2023/06/07/securing-spring-boot-applications-with-ssl)
* [SSL Smoke Test](https://github.com/spring-projects/spring-boot/tree/main/spring-boot-tests/spring-boot-smoke-tests/spring-boot-smoke-test-tomcat-ssl)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot//io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

## Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

