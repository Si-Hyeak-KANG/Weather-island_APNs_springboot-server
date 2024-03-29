package project.app.apns_server.modules.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.evnet.PushNotificationEventDto;
import project.app.apns_server.modules.service.cache.AppInfoRedisServiceImpl;
import project.app.apns_server.modules.service.weather.WeatherSearchService;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerTaskService {

    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks;
    private final Trigger trigger;

    private final TimeUnitConvertService timeUnitConvertService;
    private final AppInfoRedisServiceImpl appInfoRedisService;
    private final WeatherSearchService weatherSearchService;
    private final ApplicationEventPublisher eventPublisher;

    public void startScheduler(final String deviceToken) {
        scheduler.initialize();
        ScheduledFuture<?> task = scheduler.schedule(this.checkWeatherCurrApp(deviceToken), trigger);
        scheduledTasks.put(deviceToken, task);
    }

    public Runnable checkWeatherCurrApp(final String deviceToken) {
        return () -> {
            log.info("[checkWeatherCurrApp] ======= 스케줄러 작업을 시작합니다.(time: {}) =========", LocalDateTime.now());
            AppInfoVo appInfo = appInfoRedisService.findAppInfoByDeviceToken(deviceToken);
            log.info("\n \t [checkWeatherCurrApp] Device 토큰 = {} \n \t [checkWeatherCurrApp] Push 토큰 = {}", appInfo.getDeviceToken(), appInfo.getPushToken());
            WeatherApiResponseDto currWeather = weatherSearchService.requestCurrWeatherByLocation(appInfo.getLatitude(), appInfo.getLongitude());
            long pastTemp = appInfo.getTemp();
            long currTemp = Math.round(currWeather.mainDto().getTemp());
            log.info("[checkWeatherCurrApp] {}{} 전 온도 = {}°C, 현재 온도 = {}°C ", timeUnitConvertService.getPeriod(), timeUnitConvertService.getTimeUnitToKorean(), pastTemp, currTemp);

            if (isTemperatureDifference(pastTemp, currTemp)) {
                log.info("[checkWeatherCurrApp] {}{} 전 온도와 현재 온도 차이가 발생했습니다.", timeUnitConvertService.getPeriod(), timeUnitConvertService.getTimeUnitToKorean());
                appInfo.updateCurrTemp(currTemp);
                appInfoRedisService.saveInfo(appInfo);
                eventPublisher.publishEvent(PushNotificationEventDto.of(appInfo));
            }
        };
    }

    public void stopScheduler(String deviceToken) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(deviceToken);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(deviceToken);
        }
    }


    // 과거온도와 현재온도 차이가 있으면 true
    private boolean isTemperatureDifference(long pastTemp, long currTemp) {
        return pastTemp - currTemp != 0;
    }

}
