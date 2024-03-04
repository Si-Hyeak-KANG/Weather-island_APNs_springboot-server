FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
COPY private/APNsKey.p8 /private/APNsKey.p8
COPY private/WeatherKit.p8 /private/WeatherKit.p8
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","./app.jar"]