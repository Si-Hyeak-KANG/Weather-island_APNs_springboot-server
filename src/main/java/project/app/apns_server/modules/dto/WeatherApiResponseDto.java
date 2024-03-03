package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherApiResponseDto(@JsonProperty("currentWeather") WeatherMainDto mainDto) {

}
