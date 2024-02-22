package project.app.apns_server.modules.service.apns;

import com.turo.pushy.apns.*;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class TestApnsService {

    private ApnsClient apnsClient;

    private final static String TEAM_ID = "";
    private final static String KEY_ID = "";

    public TestApnsService() throws Exception {
        this.apnsClient = new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                .setSigningKey(
                        ApnsSigningKey.loadFromInputStream(
                                new FileInputStream(""),
                                TEAM_ID,
                                KEY_ID)
                )
                .build();
    }

    public void pushNotification() throws InterruptedException {
        long timestamp = System.currentTimeMillis() / 1000;
        String token = "";
        String payload = "{\"aps\":{\"timestamp\":"+timestamp+",\"event\":\"update\",\"content-state\":{\"temperature\":10.0}}}";
        ApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                TokenUtil.sanitizeTokenString(token),
                "",
                payload,
               new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)),
                DeliveryPriority.IMMEDIATE,
                PushType.MDM,
                "liveActivityUpdate",
                UUID.fromString("")
                );

        Future<PushNotificationResponse<ApnsPushNotification>> sendNotificationFuture = this.apnsClient.sendNotification(pushNotification);

        try {
            final PushNotificationResponse<ApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                    System.out.println("\t…and the token is invalid as of " +
                            pushNotificationResponse.getTokenInvalidationTimestamp());
                }
            }
        } catch (final ExecutionException e) {
            System.err.println("Failed to send push notification.");
            e.printStackTrace();
        }

    }
}
