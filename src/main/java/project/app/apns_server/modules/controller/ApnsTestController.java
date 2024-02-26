package project.app.apns_server.modules.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.app.apns_server.modules.dto.ApnsTestRequestDto;
import project.app.apns_server.modules.service.apns.ApplePushNotificationService;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ApnsTestController {

    private final ApplePushNotificationService pushNotificationService;

    @PostMapping("/update/live-activity")
    public ResponseEntity<String> liveActivityUpdateTest(@RequestBody @Valid ApnsTestRequestDto apnsTestRequestDto) {

        long tempTemperature = 100;
        pushNotificationService.pushNotification(apnsTestRequestDto.getPushToken(), apnsTestRequestDto.getApnsId(), tempTemperature);
        return ResponseEntity.ok("Push notification accepted by APNs gateway.");
    }
}
