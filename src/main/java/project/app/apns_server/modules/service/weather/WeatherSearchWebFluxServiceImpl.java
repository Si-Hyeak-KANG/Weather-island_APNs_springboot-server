package project.app.apns_server.modules.service.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

import java.net.URI;

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
                .doOnNext(this::convertTempUnitToCelsius)
                .single()
                .block();
    }
}
