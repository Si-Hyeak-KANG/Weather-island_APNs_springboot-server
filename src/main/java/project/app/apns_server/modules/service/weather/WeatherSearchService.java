package project.app.apns_server.modules.service.weather;

import project.app.apns_server.modules.dto.WeatherApiResponseDto;

public interface WeatherSearchService {
    WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon);

    default void convertTemperatureUnit(WeatherApiResponseDto body) {
        body.mainDto().round();
    }
}
