FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
COPY app/APNsKey.p8 /app/APNsKey.p8
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","./app.jar"]