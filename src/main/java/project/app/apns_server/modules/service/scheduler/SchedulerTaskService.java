package project.app.apns_server.modules.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ThreadPoolTaskScheduler scheduler;

    private final AppInfoRedisServiceImpl appInfoRedisService;
    private final WeatherSearchService weatherSearchService;
    private final ApplePushNotificationService applePushNotificationService;

    public void startScheduler(final String deviceToken) {
        scheduler.initialize();
        scheduler.schedule(this.checkWeatherCurrApp(deviceToken), getTrigger());
    }

    private Trigger getTrigger() {
        return new PeriodicTrigger(10, TimeUnit.SECONDS);
    }

    public Runnable checkWeatherCurrApp(final String deviceToken) {
        return () -> {
            AppInfoVo appInfo = appInfoRedisService.findAppInfoByDeviceToken(deviceToken);

            WeatherApiResponseDto currWeather = weatherSearchService.requestCurrWeatherByLocation(appInfo.getLatitude(), appInfo.getLongitude());
            double pastTemp = appInfo.getTemp();
            double currTemp = currWeather.getTemp();

            if (comparePastToCurrTemp(pastTemp, currTemp)) {
                appInfo.updateCurrTemp(currTemp);
                appInfoRedisService.saveInfo(appInfo);
                applePushNotificationService.pushNotification(appInfo.getLiveActivityToken(), currTemp);
            }
        };
    }

    // 반올림한 수가 1 or -1 이상 차이가 있으면 true
    private boolean comparePastToCurrTemp(double pastTemp, double currTemp) {
        return Math.round(currTemp) - Math.round(pastTemp) != 0;
    }

}
