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
    public void pushNotification(String pushToken, String apnsId, long temp) {

        log.debug("[ApnsConsoleServiceImpl pushNotification start] pushToken = {}, temp = {}", pushToken, temp);
        log.debug("ApnsConsoleServiceImpl pushNotification success");
    }
}
