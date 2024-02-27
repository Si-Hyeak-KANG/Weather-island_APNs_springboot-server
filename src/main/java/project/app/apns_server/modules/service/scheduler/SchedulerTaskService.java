package project.app.apns_server.modules.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.apns.ApplePushNotificationService;
import project.app.apns_server.modules.service.cache.AppInfoRedisServiceImpl;
import project.app.apns_server.modules.service.weather.WeatherSearchService;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerTaskService {

    @Value("${weather.search.scheduler.period}")
    private Long period;

    @Value("${weather.search.scheduler.time-unit}")
    private String timeUnit;

    private final ThreadPoolTaskScheduler scheduler;

    private final AppInfoRedisServiceImpl appInfoRedisService;
    private final WeatherSearchService weatherSearchService;
    private final ApplePushNotificationService applePushNotificationService;

    public void startScheduler(final String deviceToken) {
        scheduler.initialize();
        scheduler.schedule(this.checkWeatherCurrApp(deviceToken), getTrigger());
    }

    private Trigger getTrigger() {
        return new PeriodicTrigger(period, TimeUnit.valueOf(timeUnit));
    }

    public Runnable checkWeatherCurrApp(final String deviceToken) {
        return () -> {

            log.info("[SchedulerTaskService checkWeatherCurrApp] 스케줄러 작업을 시작합니다.(time: {})", LocalDateTime.now());
            AppInfoVo appInfo = appInfoRedisService.findAppInfoByDeviceToken(deviceToken);

            WeatherApiResponseDto currWeather = weatherSearchService.requestCurrWeatherByLocation(appInfo.getLatitude(), appInfo.getLongitude());
            long pastTemp = appInfo.getTemp();
            long currTemp = Math.round(currWeather.getMainDto().getTemp());
            log.info("[SchedulerTaskService checkWeatherCurrApp] 30분전 온도 = {}, 현재 온도 = {} ", pastTemp, currTemp);

            if (comparePastToCurrTemp(pastTemp, currTemp)) {
                log.info("[SchedulerTaskService checkWeatherCurrApp] 차이 발생");
                appInfo.updateCurrTemp(currTemp);
                appInfoRedisService.saveInfo(appInfo);
                applePushNotificationService.pushNotification(appInfo.getPushToken(), appInfo.getApnsId(), currTemp);
            }
        };
    }

    // 온도가 1 or -1 이상 차이가 있으면 true
    private boolean comparePastToCurrTemp(double pastTemp, double currTemp) {
        return pastTemp - currTemp != 0;
    }

}
