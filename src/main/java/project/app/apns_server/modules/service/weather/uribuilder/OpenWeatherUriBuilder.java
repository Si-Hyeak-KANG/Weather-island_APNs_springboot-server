package project.app.apns_server.modules.service.weather.uribuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import project.app.apns_server.modules.service.weather.uribuilder.WeatherUriBuilder;

import java.net.URI;

@Slf4j
@Component
public class OpenWeatherUriBuilder extends WeatherUriBuilder {

    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    private static final String CURRENT_WEATHER_SEARCH_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${open-weather.api.key}")
    private String openWeatherApiKey;

    @Override
    public URI buildUriByLocation(double lat, double lon) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(CURRENT_WEATHER_SEARCH_URL);
        uriBuilder.queryParam("lat",lat);
        uriBuilder.queryParam("lon",lon);
        uriBuilder.queryParam("appid",openWeatherApiKey);
        uriBuilder.queryParam("units","metric");

        log.info("[buildUriByLocation] Open Weather Uri Builder 사용");
        return uriBuilder.build().encode().toUri();
    }
}
