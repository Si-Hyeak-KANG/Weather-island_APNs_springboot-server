package project.app.apns_server.modules.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.app.apns_server.global.Response;
import project.app.apns_server.modules.dto.CurrAppInfoDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.AppInfoRedisService;
import project.app.apns_server.modules.service.WeatherSearchService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final AppInfoRedisService appInfoRedisService;
    private final WeatherSearchService weatherSearchService;

    @PostMapping("/test")
    public ResponseEntity<Response> saveLocationInfo(@RequestBody CurrAppInfoDto appInfo) {
        log.info("MainController CurrAppInfo : {}", appInfo.toString());
        // 1. 정보 저장 후 result dto 반환
        appInfoRedisService.saveInfo(appInfo);
        // 2. 현재 앱의 위치에 맞는 날씨 조회
        WeatherApiResponseDto weatherInfo = weatherSearchService.requestCurrWeatherByLocation(appInfo);
        // 3. 조회한 날씨 redis 저장
        appInfoRedisService.saveWeather(appInfo, weatherInfo);
        return new ResponseEntity(Response.from(appInfo, weatherInfo), HttpStatus.CREATED);
    }
}
