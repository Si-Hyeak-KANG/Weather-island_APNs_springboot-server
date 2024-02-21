# 실시간 날씨 정보 Apple Push Notification 서버

`server ver1.0.0`

설명: 앱이 백그라운드 혹은 종료되어 있을 때, LiveActivity*에 날씨 정보를 보내주기 위한 서버입니다.

> LiveActivity : 다이나믹 아일랜드 및 잠금화면에 보이는 위젯 기능. iOS16.1 이상 버전에서 사용 가능.

---

## 요구사항

- (앱, 사용자 위치, 날씨) 정보 저장 API 구현
- 일정 주기 날씨 변동 체크 기능 구현
- 날씨 변경 내용 APNs 전송 기능 구현
- LiveActivity 토큰 폐기 API 구현

## 사용기술

- Java, SpringBoot, Gradle
- Redis
- Lombok, WebFlux(webClient), Spring Scheduler, Spring-Retry
- Docker

## 참고
- [[APNs 서버] 개발 환경 명세서]()
- [날씨 OpenAPI](https://openweathermap.org/api/one-call-3#how)
- [APNs docs](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/sending_notification_requests_to_apns)

---

## GET STARTED

### 1) [Docker] 통합 개발 서버 실행

### 2) API 명세서
