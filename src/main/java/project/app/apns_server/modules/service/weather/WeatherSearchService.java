package project.app.apns_server.modules.service.weather;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

public interface WeatherSearchService {

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 2000))
    WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon);

    default void convertTemperature(WeatherApiResponseDto body) {
        body.convertToCelsius();
        body.round();
    }
}
