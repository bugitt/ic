FROM openjdk:17

COPY ./build/libs/ic-0.0.1-SNAPSHOT.jar /usr/src/myapp/alumni.jar

WORKDIR /usr/src/myapp

EXPOSE 8090

CMD ["java", "-jar", "alumni.jar"]