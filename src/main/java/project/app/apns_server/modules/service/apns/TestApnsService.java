package project.app.apns_server.modules.service.apns;

import com.eatthepath.pushy.apns.*;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TestApnsService {


    public static final String APNS_KEY_FILE_NAME = "APNsKey.p8";
    private static final String TEAM_ID = "KZ7GPQGAGJ";
    private static final String AUTH_KEY_ID = "4ZU8NK39ZW";

    @Value("${apple.push.notification.app-bundle-id}")
    private String APP_BUNDLE_ID;

    private ApnsClient apnsClient;

    public TestApnsService() throws Exception {
        this.apnsClient = new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                .setSigningKey(
                        ApnsSigningKey.loadFromInputStream(
                                new FileInputStream(APNS_KEY_FILE_NAME), TEAM_ID, AUTH_KEY_ID)
                )
                .build();
    }

    public void pushNotification() throws InterruptedException {
        long timestamp = System.currentTimeMillis() / 1000;
        String token = "40588fd3374a0c3e81a20e389983cbb9cad1b8910778422df4b99db12102ff70b04116279340c8f11dab0925af0c5c74b7bba4980784b9a0459192d504d1fff7548dea1c807ba778e7463f0b9a522249";
        String payload = "{\"aps\":{\"timestamp\":"+timestamp+",\"event\":\"update\",\"content-state\":{\"temperature\":10.0}}}";
        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                token,
                APP_BUNDLE_ID,
                payload,
                Instant.now().plus(Duration.ofHours(1)),
                DeliveryPriority.IMMEDIATE,
                PushType.LIVE_ACTIVITY,
                null,
                null
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
