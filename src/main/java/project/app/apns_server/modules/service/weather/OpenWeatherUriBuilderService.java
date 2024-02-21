package project.app.apns_server.modules.service.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class OpenWeatherUriBuilderService {

    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private static final String CURRENT_WEATHER_SEARCH_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${open-weather.api.key}")
    private String openWeatherApiKey;

    public URI buildUriByLocation(double lat, double lon) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(CURRENT_WEATHER_SEARCH_URL);
        uriBuilder.queryParam("lat",lat);
        uriBuilder.queryParam("lon",lon);
        uriBuilder.queryParam("appid",openWeatherApiKey);

        URI uri = uriBuilder.build().encode().toUri();
        return uri;
    }
}
