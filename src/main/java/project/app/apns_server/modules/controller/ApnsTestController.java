package project.app.apns_server.modules.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.app.apns_server.modules.common.dto.Response;
import project.app.apns_server.modules.dto.ApnsTestRequestDto;
import project.app.apns_server.modules.service.apns.ApplePushNotificationService;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ApnsTestController {

    private final ApplePushNotificationService applePushNotificationService;

    @PostMapping("/update/live-activity")
    public ResponseEntity<Response> liveActivityUpdateTest(@RequestBody @Valid ApnsTestRequestDto apnsTestRequestDto) {
        long tempTemperature = 100;
        applePushNotificationService.pushNotification(apnsTestRequestDto.getPushToken(), apnsTestRequestDto.getApnsId(), tempTemperature);
        return ResponseEntity.ok(Response.success("[success] Push Notification 전송을 성공하였습니다."));
    }
}
