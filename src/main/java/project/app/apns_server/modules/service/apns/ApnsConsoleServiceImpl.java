package project.app.apns_server.modules.service.apns;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class ApnsConsoleServiceImpl implements ApplePushNotificationService {

    private static final String URL = "https://api.sandbox.push.apple.com/3/device/";
    private static final String PUSH_TYPE = "liveactivity";
    private static final String PRIORITY = "10";

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public Mono<Void> pushNotification(String liveActivityToken, double temp) {

        log.info("apns 전송");

        return Mono.empty();
    }

    @Override
    public Map<String, Double> getContentState(double temp) {
        return Collections.singletonMap("temperature", temp);
    }
}
