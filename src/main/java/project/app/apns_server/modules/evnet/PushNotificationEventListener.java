package project.app.apns_server.modules.evnet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.app.apns_server.modules.service.apns.ApplePushNotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
@Async
public class PushNotificationEventListener {

    private final ApplePushNotificationService applePushNotificationService;

    @EventListener
    public void handlePushNotification(PushNotificationEventDto event) {
        applePushNotificationService.pushNotification(event.pushToken(), event.apnsId(), event.temp());
    }
}
