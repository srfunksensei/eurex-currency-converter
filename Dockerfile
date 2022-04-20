FROM amazoncorretto:11
LABEL maintainer="sr.funk.sensei@gmail.com"
COPY target/ecb-0.0.1-SNAPSHOT.jar ecb.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ecb.jar"]