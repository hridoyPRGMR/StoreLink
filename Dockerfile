FROM openjdk:21

WORKDIR /app

COPY target/StoreLink-0.0.1-SNAPSHOT.jar /app/StoreLink-0.0.1-SNAPSHOT.jar
COPY jwt-secret.key /app/jwt-secret.key
COPY .env /app/.env

EXPOSE 8080

ENTRYPOINT [ "java","-jar","StoreLink-0.0.1-SNAPSHOT.jar" ]

