FROM openjdk:17

COPY ./build/libs/ic-0.0.1-all.jar /usr/src/myapp/alumni.jar

COPY /binary/mc /bin/mc

WORKDIR /usr/src/myapp

EXPOSE 8090

CMD ["java", "-jar", "alumni.jar"]