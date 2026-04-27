FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080
ADD target/filecloud-0.0.1-SNAPSHOT.jar filecloud.jar
ENTRYPOINT ["java","-jar","/filecloud.jar"]
