package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @OPEN-API-REFERENCE : https://openweathermap.org/current
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponseDto {

    @JsonProperty("main")
    private WeatherMainDto mainDto;

}
