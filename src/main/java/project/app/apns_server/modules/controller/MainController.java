package project.app.apns_server.modules.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.app.apns_server.modules.common.dto.Response;
import project.app.apns_server.modules.dto.AppInfoRequestDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.cache.AppInfoRedisService;
import project.app.apns_server.modules.service.scheduler.SchedulerTaskService;
import project.app.apns_server.modules.service.weather.WeatherSearchService;
import project.app.apns_server.modules.vo.AppInfoVo;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final SchedulerTaskService schedulerTaskService;
    private final AppInfoRedisService appInfoRedisService;
    private final WeatherSearchService weatherSearchService;

    @PostMapping("/init/app/info")
    public ResponseEntity<Response> saveAppAndWeatherInitInfo(@RequestBody AppInfoRequestDto appInfoRequest) {

        // 현재 앱의 위치에 맞는 날씨 조회
        WeatherApiResponseDto weatherInfo = weatherSearchService
                .requestCurrWeatherByLocation(appInfoRequest.getLatitude(), appInfoRequest.getLongitude());

        // 조회한 날씨와 요청한 앱의 정보 Redis 저장
        AppInfoVo appInfo = AppInfoVo.of(appInfoRequest, weatherInfo);
        boolean isUpdate = appInfoRedisService.saveInfo(appInfo);
        if(!isUpdate) schedulerTaskService.startScheduler(appInfo.getDeviceToken());
        return new ResponseEntity<>(Response.success(appInfo), HttpStatus.CREATED);
    }
}
