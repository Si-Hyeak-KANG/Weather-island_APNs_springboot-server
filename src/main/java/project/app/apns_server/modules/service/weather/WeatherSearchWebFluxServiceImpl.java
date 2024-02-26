package project.app.apns_server.modules.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherSearchWebFluxServiceImpl implements WeatherSearchService {

    private final WebClient.Builder webClientBuilder;
    private final OpenWeatherUriBuilderService openWeatherUriBuilderService;

    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {
        URI uri = openWeatherUriBuilderService.buildUriByLocation(lat, lon);
        return webClientBuilder.build()
                .method(HttpMethod.GET)
                .uri(uri)
                .retrieve()
                .bodyToMono(WeatherApiResponseDto.class)
                .doOnNext(this::convertTemperature)
                .doOnNext(response -> log.info("[WeatherSearchWebFluxServiceImpl requestCurrWeatherByLocation] success"))
                .doOnNext(response -> log.debug("[WeatherSearchWebFluxServiceImpl requestCurrWeatherByLocation] response temp = {}", response.getTemp()))
                .block();
    }
}
