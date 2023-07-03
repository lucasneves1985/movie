FROM openjdk:11
EXPOSE 8080
ADD ./target/goldenRaspberryAwards-1.0.jar goldenRaspberryAwards.jar
ENTRYPOINT ["java","-jar","/goldenRaspberryAwards.jar"]