package project.app.apns_server.modules.service.apns;

import com.eatthepath.pushy.apns.*;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.service.ObjectMapperService;
import project.app.apns_server.modules.service.apns.dto.ApnRequestDto;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplePushNotificationServiceImpl implements ApplePushNotificationService {

    private final ApnsClient apnsClient;
    private final ObjectMapperService objectMapperService;

    @Value("${apple.push.notification.app-bundle-id}")
    private String APP_BUNDLE_ID;

    @Override
    public Mono<Void> pushNotification(String pushToken, double temperature) {

        ApnRequestDto dto = ApnRequestDto.of(
                System.currentTimeMillis() / 1000,
                "update",
                Collections.singletonMap("temperature", temperature)
        );

        String payload = objectMapperService.serializeApnRequestDto(dto);
        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                pushToken,
                APP_BUNDLE_ID,
                payload,
                Instant.now().plus(Duration.ofHours(1)),
                DeliveryPriority.IMMEDIATE,
                PushType.LIVE_ACTIVITY,
                "liveActivityUpdate",
                UUID.fromString("423C6AE3-0EFB-48AD-B721-FDE7BEAC8CEB")
        );

        Future<PushNotificationResponse<ApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        try {
            final PushNotificationResponse<ApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                if (pushNotificationResponse.getTokenInvalidationTimestamp().isPresent()) {
                    System.out.println("\t…and the token is invalid as of " +
                            pushNotificationResponse.getTokenInvalidationTimestamp());
                }
            }
        } catch (final ExecutionException e) {
            System.err.println("Failed to send push notification.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Mono.empty();
    }
}
