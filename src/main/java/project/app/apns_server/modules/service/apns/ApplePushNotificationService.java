package project.app.apns_server.modules.service.apns;

import reactor.core.publisher.Mono;

public interface ApplePushNotificationService {

    void pushNotification(String pushToken, String apnsId, long temp);
}
