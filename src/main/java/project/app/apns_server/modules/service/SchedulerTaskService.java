package project.app.apns_server.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerTaskService {

    private final ThreadPoolTaskScheduler scheduler;

    private final AppInfoRedisService appInfoRedisService;
    private final WeatherSearchService weatherSearchService;

    // TODO : APNs push 서비스 주입

    public void startScheduler(final String token) {
        scheduler.initialize();
        scheduler.schedule(this.checkWeatherCurrApp(token), getTrigger());
    }

    private Trigger getTrigger() {
        return new PeriodicTrigger(30, TimeUnit.SECONDS);
    }

    public Runnable checkWeatherCurrApp(final String deviceToken) {
        return () -> {
            log.info("checkWeatherCurrApp-deviceToken: {}", deviceToken);

            AppInfoVo appInfoVo = appInfoRedisService.findAppInfoByDeviceToken(deviceToken);
            if (appInfoVo != null) {
                WeatherApiResponseDto currWeather = weatherSearchService.requestCurrWeatherByLocation(appInfoVo.getLatitude(), appInfoVo.getLongitude());

                long pastTemp = Math.round(appInfoVo.getTemp());
                long currTemp = Math.round(currWeather.getTemp());

                log.info("[{} - {} 스케줄러 동작]", deviceToken, LocalDateTime.now());

                if (differenceBetweenPastAndCurrTemp(pastTemp, currTemp)) {
                    log.info("차이 발생");
                    appInfoVo.updateCurrTemp(currTemp);
                    try {
                        appInfoRedisService.saveInfo(appInfoVo);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    // TODO APNs push
                }
            } else {
                log.warn("AppInfoVo가 null입니다.");
            }
        };
    }

    // 반올림한 수가 1 or -1 이상 차이가 있으면 true
    private static boolean differenceBetweenPastAndCurrTemp(long pastTemp, long currTemp) {
        return currTemp - pastTemp != 0;
    }

}
