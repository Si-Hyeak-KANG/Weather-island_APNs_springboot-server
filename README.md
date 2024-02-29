# 실시간 날씨 정보 Apple Push Notification 서버

`server ver1.0.0`

설명: 앱이 백그라운드 혹은 종료되어 있을 때, LiveActivity*에 날씨 정보를 보내주기 위한 서버입니다.

> LiveActivity : 다이나믹 아일랜드 및 잠금화면에 보이는 위젯 기능. iOS16.1 이상 버전에서 사용 가능.

---


### 요구사항

- (앱, 사용자 위치, 날씨) 정보 저장 API 구현
- 일정 주기 날씨 변동 체크 기능 구현
- 날씨 변경 내용 APNs 전송 기능 구현
- LiveActivity 토큰 폐기 API 구현

### 사용기술

- Java17, SpringBoot, Gradle
- Redis
- Lombok, WebFlux(webClient), Spring Scheduler, Spring-Retry, aop, validation, ObjectMapper
- [eatthepath.pushy(APNs)](https://github.com/jchambers/pushy)
- Docker

### API 명세서

- 문서화 툴 : Postman
- https://documenter.getpostman.com/view/15157402/2sA2r9XPRY

---
<br>

# GET STARTED
  
## [Docker] 통합 개발 서버 실행

> 터미널에서 진행

- docker 이미지 내려받기
  - redis : `docker pull zlcls456/weather-island-redis:latest`
  - app   : `docker pull zlcls456/weather-island-app:latest`

- 이미지를 제대로 받았는지 확인
  - `docker images`
 
- 도커 이미지 실행
> -d : 백그라운드 실행
> 
> if) 만약 제대로 실행되지 않는다면, `\` 제거 후 한줄로 입력해주세요. 또한 오타가 없는지 확인해주세요.
  - redis :

        docker run --name weather-island-redis -d -p 6380:6379 zlcls456/weather-island-redis
    
  - app :
    
    > `$NAME` 형태는 터미널에서 환경변수로 값 기입     

        docker run \
        --name weather-island-app \
        -e SPRING_PROFILES_ACTIVE=dev \
        -e OPEN_WEATHER_API_KEY=$WEATHER_API_KEY \
        -e APP_BUNDLE_ID=$BUNDLE_ID \
        -e AUTH_KEY_ID=$AUTH_KEY_ID \
        -e TEAM_ID=$TEAM_ID \
        -p 8081:8080 zlcls456/weather-island-app

- 정상 실행 결과
  
![image](https://github.com/Si-Hyeak-KANG/Weather-island_APNs_springboot-server/assets/79829085/85b15ca1-d487-4c51-a909-4f124b60a1da)

- api 요청 테스트
  - host : 0.0.0.0
  - port : 8081 

---

## 참고링크
- [[APNs 서버] 개발 환경 명세서](https://github.com/Si-Hyeak-KANG/Weather-island_APNs_springboot-server/wiki/%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EB%AA%85%EC%84%B8%EC%84%9C)
- [날씨 OpenAPI](https://openweathermap.org/api/one-call-3#how)
- [APNs docs](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/sending_notification_requests_to_apns)
