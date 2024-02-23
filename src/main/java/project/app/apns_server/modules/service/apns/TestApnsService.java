package project.app.apns_server.modules.service.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.ApnsPushNotification;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
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
        String token = "e6a0876e456764f1de7cf4aab245f9cf595ca79c836d6f66f96da438f992b952";
        String payload = "{\"aps\":{\"timestamp\":"+timestamp+",\"alert\":\"check\",\"event\":\"update\",\"content-state\":{\"temperature\":10.0}}}";
        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, APP_BUNDLE_ID, payload);

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
