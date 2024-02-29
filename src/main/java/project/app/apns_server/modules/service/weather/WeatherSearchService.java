package project.app.apns_server.modules.service.weather;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

public interface WeatherSearchService {
    WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon);

    default void convertTemperatureUnit(WeatherApiResponseDto body) {
        //body.getMainDto().convertToCelsius();
        body.getMainDto().round();
    }
}
