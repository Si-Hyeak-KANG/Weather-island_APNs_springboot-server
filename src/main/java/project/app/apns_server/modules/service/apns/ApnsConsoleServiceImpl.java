package project.app.apns_server.modules.service.apns;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
//@Service
//@Profile({"local", "test"})
public class ApnsConsoleServiceImpl implements ApplePushNotificationService {

    @Override
    public Mono<Void> pushNotification(String liveActivityToken, double temp) {

        log.debug("[ApnsConsoleServiceImpl pushNotification start] liveActivityToken = {}, temp = {}", liveActivityToken, temp);
        log.debug("ApnsConsoleServiceImpl pushNotification success");
        return Mono.empty();
    }

    public Map<String, Double> getContentState(double temp) {
        return Collections.singletonMap("temperature", temp);
    }
}
