package project.app.apns_server.modules.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherSearchService {

    private final RestTemplate restTemplate;
    private final OpenWeatherUriBuilderService openWeatherUriBuilderService;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    public WeatherApiResponseDto requestCurrWeatherByLocationCoordinate(double lat, double lon) {

        URI uri = openWeatherUriBuilderService.buildUriByLocation(lat, lon);

        WeatherApiResponseDto body = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, WeatherApiResponseDto.class).getBody();
        body.convertUnit();
        log.info("requestCurrWeatherByLocation temp = {}",body.getMainDto());
        return body;
    }
}
