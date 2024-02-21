package project.app.apns_server.modules.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
public class WeatherSearchRestTemplateServiceImpl implements WeatherSearchService {

    private final RestTemplate restTemplate;
    private final OpenWeatherUriBuilderService openWeatherUriBuilderService;

    @Override
    public WeatherApiResponseDto requestCurrWeatherByLocation(double lat, double lon) {

        URI uri = openWeatherUriBuilderService.buildUriByLocation(lat, lon);

        WeatherApiResponseDto body =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        WeatherApiResponseDto.class).getBody();

        convertTempUnitToCelsius(body);
        return body;
    }

}
