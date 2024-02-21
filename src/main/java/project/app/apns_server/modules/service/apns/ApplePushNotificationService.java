package project.app.apns_server.modules.service.apns;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ApplePushNotificationService {

    Mono<Void> pushNotification(String liveActivityToken, double temp);

    Map<String, Double> getContentState(double temp);
}
