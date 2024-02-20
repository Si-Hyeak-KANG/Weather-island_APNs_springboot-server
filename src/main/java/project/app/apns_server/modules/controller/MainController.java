package project.app.apns_server.modules.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.app.apns_server.global.dto.Response;
import project.app.apns_server.modules.dto.AppInfoRequestDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.AppInfoRedisService;
import project.app.apns_server.modules.service.SchedulerTaskService;
import project.app.apns_server.modules.service.WeatherSearchService;
import project.app.apns_server.modules.vo.AppInfoVo;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final AppInfoRedisService appInfoRedisService;
    private final WeatherSearchService weatherSearchService;
    private final SchedulerTaskService schedulerTaskService;

    @PostMapping("/init/app/info")
    public ResponseEntity<Response> saveAppAndWeatherInitInfo(@RequestBody AppInfoRequestDto appInfo) {
        log.info("MainController CurrAppInfo : {}", appInfo.toString());
        // 현재 앱의 위치에 맞는 날씨 조회
        WeatherApiResponseDto weatherInfo = weatherSearchService.requestCurrWeatherByLocationCoordinate(appInfo.getLatitude(), appInfo.getLongitude());
        // 조회한 날씨와 요청한 앱의 데이터 Redis 저장
        AppInfoVo result = AppInfoVo.of(appInfo, weatherInfo);
        appInfoRedisService.saveInfo(result);

        schedulerTaskService.startScheduler(appInfo.getLiveActivityToken());

        log.info("MainController success");
        return new ResponseEntity<>(Response.success(result), HttpStatus.CREATED);
    }
}
