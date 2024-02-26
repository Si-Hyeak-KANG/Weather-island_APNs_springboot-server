package project.app.apns_server.modules.service.apns;

import reactor.core.publisher.Mono;

public interface ApplePushNotificationService {

    Mono<Void> pushNotification(String liveActivityToken, double temp);
}
