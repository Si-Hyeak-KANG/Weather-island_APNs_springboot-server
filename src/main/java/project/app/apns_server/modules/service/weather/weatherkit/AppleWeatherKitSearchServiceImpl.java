package project.app.apns_server.modules.service.weather.weatherkit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.service.weather.WeatherSearchService;
import project.app.apns_server.modules.service.weather.uribuilder.WeatherKitUriBuilder;

import java.net.URI;

@Slf4j
@Profile({"dev", "prod"})
@Service
@RequiredArgsConstructor
public class AppleWeatherKitSearchServiceImpl implements WeatherSearchService {

    private static final String BEARER = "Bearer "; // TODO 이름변경

    @Value("${apple.weather.kit.key-id}")
    private String WEATHER_KIT_KEY_ID;

    @Value("${apple.team-id}")
    private String TEAM_ID;

    @Value("${apple.weather.kit.service-id}")
    private String WEATHER_KIT_SERVICE_ID;

    private final WebClient.Builder webClientBuilder;
    private final WeatherKitUriBuilder weatherKitUriBuilder;
    private final WeatherKitJwtTokenizer jwtTokenizer;

    @Override
    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {

        URI uri = weatherKitUriBuilder.buildUriByLocation(lat, lon);
        try {
            return webClientBuilder.build()
                    .method(HttpMethod.GET)
                    .uri(uri)
                    .header("Authorization",
                            BEARER.concat(jwtTokenizer.createJwtToken(
                                    WEATHER_KIT_KEY_ID,
                                    TEAM_ID,
                                    WEATHER_KIT_SERVICE_ID)))
                    .retrieve()
                    .bodyToMono(WeatherApiResponseDto.class)
                    .doOnNext(this::convertTemperatureUnit)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
