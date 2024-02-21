package project.app.apns_server.modules.service.apns;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * curl \
 * --header "apns-topic: {앱번들ID}.push-type.liveactivity" \
 * --header "apns-push-type: liveactivity" \
 * --header "apns-priority: 10" \
 * --header "authorization: bearer $AUTHENTICATION_TOKEN" \
 * --data '{
 * "aps": {
 * "timestamp": '$(date +%s)',
 * "event": "update",
 * "content-state": {
 * // 필요한 정보 추가
 * }
 * }
 * }' \
 * --http2 https://api.sandbox.push.apple.com/3/device/$ACTIVITY_PUSH_TOKEN
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApnsRestTemplateService implements ApplePushNotificationService {

    @Value("${apple.push.notification.auth-token}")
    private static String AUTHENTICATION_TOKEN;

    @Value("${apple.push.notification.app-bundle-id}")
    private static String APP_BUNDLE_ID;

    private static final String URL = "https://api.sandbox.push.apple.com/3/device/";
    private static final String PUSH_TYPE = "liveactivity";
    private static final String PRIORITY = "10";

    private final RestTemplate restTemplate;

    @Override
    public void pushNotification(String liveActivityToken, double temp) {

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

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + liveActivityToken, HttpMethod.POST, httpEntity, String.class);

        log.info("apns reponse = {}", response.getBody());

    }

    private static Map<String, Object> getContentState(double temp) {
        return Collections.singletonMap("temperature", temp);
    }
}
