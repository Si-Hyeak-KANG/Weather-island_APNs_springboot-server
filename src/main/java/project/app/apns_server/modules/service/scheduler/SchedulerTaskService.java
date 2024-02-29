package project.app.apns_server.modules.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.evnet.PushNotificationEventDto;
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
    private final ApplicationEventPublisher eventPublisher;

    public void startScheduler(final String deviceToken) {
        scheduler.initialize();
        scheduler.schedule(this.checkWeatherCurrApp(deviceToken), getTrigger());
    }

    private Trigger getTrigger() {
        return new PeriodicTrigger(period, TimeUnit.valueOf(timeUnit));
    }

    public Runnable checkWeatherCurrApp(final String deviceToken) {
        return () -> {

            log.info("[checkWeatherCurrApp] 스케줄러 작업을 시작합니다.(time: {})", LocalDateTime.now());
            AppInfoVo appInfo = appInfoRedisService.findAppInfoByDeviceToken(deviceToken);

            WeatherApiResponseDto currWeather = weatherSearchService.requestCurrWeatherByLocation(appInfo.getLatitude(), appInfo.getLongitude());
            long pastTemp = appInfo.getTemp();
            long currTemp = Math.round(currWeather.getMainDto().getTemp());
            log.info("\n \t [checkWeatherCurrApp] Device 토큰 = {} \n \t [checkWeatherCurrApp] Push 토큰 = {}", appInfo.getDeviceToken(), appInfo.getPushToken());
            log.info("[checkWeatherCurrApp] {}{} 전 온도 = {}°C, 현재 온도 = {}°C ",period, getTimeUnit(), pastTemp, currTemp);

            if (isTemperatureDifference(pastTemp, currTemp)) {
                log.info("[checkWeatherCurrApp] {}{} 전 온도와 현재 온도 차이가 발생했습니다.", period, getTimeUnit());
                appInfo.updateCurrTemp(currTemp);
                appInfoRedisService.saveInfo(appInfo);
                eventPublisher.publishEvent(PushNotificationEventDto.of(appInfo.getPushToken(), appInfo.getApnsId(), currTemp));
            }
        };
    }

    private String getTimeUnit() {
        return switch (timeUnit) {
            case "MINUTES" -> "분";
            case "SECONDS" -> "초";
            default -> timeUnit;
        };
    }

    // 온도가 1 or -1 이상 차이가 있으면 true
    private boolean isTemperatureDifference(long pastTemp, long currTemp) {
        return pastTemp - currTemp != 0;
    }

}
