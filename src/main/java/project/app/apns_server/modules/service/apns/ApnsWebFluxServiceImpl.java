package project.app.apns_server.modules.service.apns;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ApnsWebFluxServiceImpl implements ApplePushNotificationService {

    @Value("${apple.push.notification.auth-token}")
    private static String AUTHENTICATION_TOKEN;

    @Value("${apple.push.notification.app-bundle-id}")
    private static String APP_BUNDLE_ID;

    private static final String URL = "https://api.sandbox.push.apple.com/3/device/";
    private static final String PUSH_TYPE = "liveactivity";
    private static final String PRIORITY = "10";

    private final WebClient.Builder webClientBuilder;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public Mono<Void> pushNotification(String liveActivityToken, double temp) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apns-topic", APP_BUNDLE_ID + ".push-type." + PUSH_TYPE);
        headers.set("apns-push-type", PUSH_TYPE);
        headers.set("apns-priority", PRIORITY);
        headers.set("authorization", "bearer " + AUTHENTICATION_TOKEN);


        Map<String, Object> aps = new HashMap<>();
        aps.put("timestamp", System.currentTimeMillis() / 1000L);
        aps.put("event", "update");
        aps.put("content-state", getContentState(temp));

        Map<String, Object> body = Collections.singletonMap("aps", aps);

        return webClientBuilder.build()
                .post()
                .uri(URL + liveActivityToken)
                .headers(h -> h.addAll(headers))
                .body(Mono.just(body), new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> log.info("apns response = {}", response))
                .then();
    }

    @Override
    public Map<String, Double> getContentState(double temp) {
        return Collections.singletonMap("temperature", temp);
    }
}
