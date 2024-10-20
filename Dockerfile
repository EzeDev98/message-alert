FROM openjdk:17
COPY ./target/wm-notification-module-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app/
RUN sh -c 'touch wm-notification-module-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","wm-notification-module-0.0.1-SNAPSHOT.jar"]