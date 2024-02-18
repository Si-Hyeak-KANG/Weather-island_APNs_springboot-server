package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.javac.Main;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @OPEN-API-REFERENCE : https://openweathermap.org/current
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiResponseDto {

    @JsonProperty("main")
    private MainDto mainDto;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MainDto {

        @JsonProperty("temp")
        private double temp;
    }
}
