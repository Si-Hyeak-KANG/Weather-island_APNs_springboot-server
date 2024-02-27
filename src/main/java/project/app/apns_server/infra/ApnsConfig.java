package project.app.apns_server.infra;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class ApnsConfig {

    private static final String APNS_KEY_FILE = "app/APNsKey.p8";

    @Value("${apple.push.notification.auth-key-id}")
    private String AUTH_KEY_ID;

    @Value("${apple.push.notification.team-id}")
    private String TEAM_ID;

    @Bean
    public ApnsClient apnsClient() {
        try {
            return new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setSigningKey(
                            ApnsSigningKey.loadFromInputStream(
                                    new FileInputStream(APNS_KEY_FILE), TEAM_ID, AUTH_KEY_ID)
                    ).build();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
