package project.app.apns_server.modules.service.weather.uribuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class WeatherKitUriBuilder extends WeatherUriBuilder {

    private static final String CURRENT_WEATHER_SEARCH_URL = "https://weatherkit.apple.com/api/v1/weather/ko";
    public static final String CURRENT_WEATHER = "currentWeather";

    @Override
    public URI buildUriByLocation(double lat, double lon) {

        String url = CURRENT_WEATHER_SEARCH_URL + toUrlString(lat) + toUrlString(lon);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        uriBuilder.queryParam("dataSets", CURRENT_WEATHER);
        log.info("[buildUriByLocation] Apple WeatherKit Uri Builder 사용");
        return uriBuilder.build().encode().toUri();
    }

    private static String toUrlString(double val) {
        return "/".concat(String.valueOf(val));
    }
}
