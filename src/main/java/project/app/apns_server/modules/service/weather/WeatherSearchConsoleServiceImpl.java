package project.app.apns_server.modules.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.dto.WeatherMainDto;
import project.app.apns_server.modules.service.ObjectMapperService;

import java.net.URI;

@Slf4j
@Service
@Profile({"local","test"})
@RequiredArgsConstructor
public class WeatherSearchConsoleServiceImpl implements WeatherSearchService {

    private final OpenWeatherUriBuilderService openWeatherUriBuilderService;
    private final ObjectMapperService objectMapperService;

    @Override
    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {

        URI uri = openWeatherUriBuilderService.buildUriByLocation(lat, lon);
        log.info("[requestCurrWeatherByLocation] 날씨 조회 URI = {}", uri);
        WeatherApiResponseDto body = new WeatherApiResponseDto(new WeatherMainDto(273.13)); // kelvin
        convertTemperatureUnit(body);
        log.info("[requestCurrWeatherByLocation] 날씨 조회 결과");
        log.info("[requestCurrWeatherByLocation] body = {}", objectMapperService.serializeWeatherApiResponseDto(body));
        log.info("[requestCurrWeatherByLocation] 온도 = {}°C", body.getMainDto().getTemp()); // 0도
        return body;
    }

}
