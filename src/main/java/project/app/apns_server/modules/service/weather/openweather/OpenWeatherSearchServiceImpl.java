package project.app.apns_server.modules.service.weather.openweather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.weather.WeatherSearchService;
import project.app.apns_server.modules.service.weather.uribuilder.OpenWeatherUriBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
public class OpenWeatherSearchServiceImpl implements WeatherSearchService {

    private final WebClient.Builder webClientBuilder;
    private final OpenWeatherUriBuilder openWeatherUriBuilder;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 2000))
    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {
        URI uri = openWeatherUriBuilder.buildUriByLocation(lat, lon);
        return webClientBuilder.build()
                .method(HttpMethod.GET)
                .uri(uri)
                .retrieve()
                .bodyToMono(WeatherApiResponseDto.class)
                .doOnNext(response -> log.info("[requestCurrWeatherByLocation] 성공적으로 날씨를 조회하였습니다."))
                .doOnNext(response -> log.info("[requestCurrWeatherByLocation] 날씨 조회 결과, 온도= {}°C", response.mainDto().getTemp()))
                .doOnNext(this::convertTemperatureUnit)
                .doOnError(response -> log.error("날씨 조회를 실패하였습니다.(message={})", response.getMessage()))
                .block();
    }
}
