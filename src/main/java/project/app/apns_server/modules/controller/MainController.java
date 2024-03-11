package project.app.apns_server.modules.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/init/app/info")
    public ResponseEntity<Response> saveAppAndWeatherInitInfo(@RequestBody @Valid AppInfoRequestDto appInfoRequest) {
        AppInfoVo appInfo = AppInfoVo.of(appInfoRequest);
        boolean isUpdate = appInfoRedisService.saveInfo(appInfo);
        if(!isUpdate) schedulerTaskService.startScheduler(appInfo.getDeviceToken());
        return new ResponseEntity<>(Response.success(appInfo), HttpStatus.CREATED);
    }
}
