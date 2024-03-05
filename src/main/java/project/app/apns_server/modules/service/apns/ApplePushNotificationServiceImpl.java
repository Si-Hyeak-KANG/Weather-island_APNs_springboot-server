package project.app.apns_server.modules.service.apns;

import com.eatthepath.pushy.apns.*;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.common.exception.exceptionCode.BusinessLogicException;
import project.app.apns_server.modules.common.exception.exceptionCode.ExceptionCode;
import project.app.apns_server.modules.service.ObjectMapperService;
import project.app.apns_server.modules.dto.ApnRequestDto;
import project.app.apns_server.modules.service.cache.AppInfoRedisService;
import project.app.apns_server.modules.service.scheduler.SchedulerTaskService;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplePushNotificationServiceImpl implements ApplePushNotificationService {

    private final ApnsClient apnsClient;
    private final ObjectMapperService objectMapperService;
    private final AppInfoRedisService appInfoRedisService;
    private final SchedulerTaskService schedulerTaskService;

    @Value("${apple.app-id}")
    private String APP_ID;

    @Override
    public void pushNotification(AppInfoVo appInfoVo) {

        log.info("[pushNotification]=====Push Notification 전송=========");

        ApnRequestDto dto = ApnRequestDto.of(
                System.currentTimeMillis() / 1000,
                "update",
                Collections.singletonMap("temperature", toString(appInfoVo.getTemp()))
        );

        String payload = objectMapperService.serializeApnRequestDto(dto);
        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                appInfoVo.getPushToken(),
                APP_ID + ".push-type.liveactivity",
                payload,
                Instant.now().plus(Duration.ofHours(1)),
                DeliveryPriority.IMMEDIATE,
                PushType.LIVE_ACTIVITY
        );

        Future<PushNotificationResponse<ApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);
        try {
            final PushNotificationResponse<ApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                log.info("[success] Push Notification 전송을 성공하였습니다.");
            } else {

                Optional<String> reason = pushNotificationResponse.getRejectionReason();
                BusinessLogicException exception = new BusinessLogicException(ExceptionCode.APNS_PUSH_NOTIFICATION_FAIL, reason.get());

                if (pushNotificationResponse.getTokenInvalidationTimestamp().isPresent()) {
                    exception = new BusinessLogicException(ExceptionCode.APNS_PUSH_TOKEN_EXPIRED, "만료날짜: " + String.valueOf(pushNotificationResponse.getTokenInvalidationTimestamp().get()));
                }
                String deviceToken = appInfoVo.getDeviceToken();
                if(deviceToken !=null) {
                    log.error(exception.getErrorMessage());
                    appInfoRedisService.deleteAppInfoByDeviceToken(deviceToken);
                    schedulerTaskService.stopScheduler(deviceToken);
                } else throw exception;
            }
        } catch (final ExecutionException e) {
            throw new BusinessLogicException(ExceptionCode.APNS_PUSH_NOTIFICATION_FAIL, e.getMessage());
        } catch (InterruptedException e) {
            throw new BusinessLogicException(ExceptionCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static String toString(long temperature) {
        return String.valueOf(temperature).concat("°C");
    }
}
