package project.app.apns_server.modules.service.weather;

import lombok.extern.slf4j.Slf4j;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

@Slf4j
public class WeatherSearchConsoleServiceImpl implements WeatherSearchService {

    @Override
    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {

        MockWeatherResponse mock = new MockWeatherResponse();
        convertTempUnitToCelsius(mock);
        // TODO console 출력
        return mock;
    }
}
