# 실시간 날씨 정보 Apple Push Notification 서버

`server ver1.0.0`

설명: 앱이 백그라운드 혹은 종료되어 있을 때, LiveActivity*에 날씨 정보를 보내주기 위한 서버입니다.

> LiveActivity : 다이나믹 아일랜드 및 잠금화면에 보이는 위젯 기능. iOS16.1 이상 버전에서 사용 가능.

---


### 요구사항

- (앱, 사용자 위치, 날씨) 정보 저장 API 구현
- 일정 주기 날씨 변동 체크 기능 구현
- 날씨 변경 내용 APNs 전송 기능 구현
- 날씨 스케줄러 중단 및 토큰 폐기 기능 구현

### 사용기술

- Java17, SpringBoot, Gradle
- Redis
- Lombok, WebFlux(webClient), Scheduler, Spring-Retry, AOP, Spring Validation, ObjectMapper
- Base64(ES256), JJWT 
- [eatthepath.pushy(APNs)](https://github.com/jchambers/pushy)
- [WeatherKit API](https://developer.apple.com/documentation/weatherkitrestapi)
- Docker

### API 명세서

- 문서화 툴 : Postman
- https://documenter.getpostman.com/view/15157402/2sA2r9XPRY

---
<br>

# 🏃‍♂️ GET STARTED
  
## [Docker] 통합개발 서버 실행하기

> !) CLI 환경에서 진행합니다. 
> 
> ‼) 추가로 OS, 프로세서에 따라 다른 build platform 을 갖기 때문에 확인하여 받아주세요.

### 1) docker 이미지 내려받기

- Intel 칩, Window, ubuntu 등

  
      docker pull zlcls456/weather-island-redis-amd64:latest

      docker pull zlcls456/weather-island-app-amd64:latest
    
- Apple 칩


      docker pull zlcls456/weather-island-redis-arm64:latest

      docker pull zlcls456/weather-island-app-arm64:latest

### 2) 이미지를 제대로 받았는지 확인
  - `docker images`
 
### 3) 도커 이미지 실행

> Intel 칩, Window, ubuntu 등

- redis :

      docker run --name weather-island-redis -d -p 6380:6379 zlcls456/weather-island-redis-amd64
    
- app :
    
    > `$NAME` 형태는 터미널에서 환경변수로 값 기입     

      docker run \
      --name weather-island-app \
      --network weather-island \
      -e SPRING_PROFILES_ACTIVE=dev \
      -e TEAM_ID=$TEAM_ID \
      -e APP_ID=$APP_ID \
      -e SERVICE_ID=$SERVICE_ID \
      -e APNS_KEY_ID=$APNS_KEY_ID \
      -e WEATHER_KIT_KEY_ID=$WEATHER_KIT_KEY_ID \
      -p 8081:8080 zlcls456/weather-island-app-amd64

<br>

> Apple 칩

- redis :

      docker run --name weather-island-redis -d -p 6380:6379 zlcls456/weather-island-redis-arm64

- app :

      docker run \
      --name weather-island-app \
      --network weather-island \
      -e SPRING_PROFILES_ACTIVE=dev \
      -e TEAM_ID=$TEAM_ID \
      -e APP_ID=$APP_ID \
      -e SERVICE_ID=$SERVICE_ID \
      -e APNS_KEY_ID=$APNS_KEY_ID \
      -e WEATHER_KIT_KEY_ID=$WEATHER_KIT_KEY_ID \
      -p 8081:8080 zlcls456/weather-island-app-arm64

<br>

> -d : 백그라운드 실행 명령어
>
> if) 만약 제대로 실행되지 않는다면, `\` 제거 후 한줄로 입력해주세요. 또한 오타가 없는지 확인해주세요.

### 4) 정상 실행 결과

- 실행중인 컨테이너 확인 명령어

      docker ps

  ![image](https://github.com/Si-Hyeak-KANG/Weather-island_APNs_springboot-server/assets/79829085/3cb2168f-4155-4790-b987-1dc47bdd733a)

  
![image](https://github.com/Si-Hyeak-KANG/Weather-island_APNs_springboot-server/assets/79829085/85b15ca1-d487-4c51-a909-4f124b60a1da)

### 5) api 요청 테스트
  - host : 0.0.0.0
  - port : 8081

---

## 참고링크
- [[APNs 서버] 개발 환경 명세서](https://github.com/Si-Hyeak-KANG/Weather-island_APNs_springboot-server/wiki/%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EB%AA%85%EC%84%B8%EC%84%9C)
- [WeatherKit Docs](https://developer.apple.com/documentation/weatherkitrestapi)
- [APNs Docs](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/sending_notification_requests_to_apns)
- [날씨 OpenAPI](https://openweathermap.org/api/one-call-3#how)
