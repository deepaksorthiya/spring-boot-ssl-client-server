# Stage 1: Build Stage
FROM bellsoft/liberica-runtime-container:jdk-21-stream-musl AS builder

WORKDIR /home/app
COPY . /home/app/spring-boot-ssl-client-server
WORKDIR /home/app/spring-boot-ssl-client-server
RUN  chmod +x mvnw && ./mvnw -Dmaven.test.skip=true clean package

# Stage 2: Layer Tool Stage
FROM bellsoft/liberica-runtime-container:jdk-21-cds-slim-musl AS optimizer

WORKDIR /home/app
COPY --from=builder /home/app/spring-boot-ssl-client-server/target/*.jar spring-boot-ssl-client-server.jar
RUN java -Djarmode=tools -jar spring-boot-ssl-client-server.jar extract --layers --launcher

# Stage 3: Final Stage
FROM bellsoft/liberica-runtime-container:jre-21-stream-musl

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
COPY --from=optimizer /home/app/spring-boot-ssl-client-server/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-ssl-client-server/spring-boot-loader/ ./
COPY --from=optimizer /home/app/spring-boot-ssl-client-server/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-ssl-client-server/application/ ./