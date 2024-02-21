package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @OPEN-API-REFERENCE : https://openweathermap.org/current
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponseDto {

    public static final double DEFAULT_KELVIN = 273.15;

    @JsonProperty("main")
    private MainDto mainDto;

    public void convertToCelsius() {
        this.mainDto.temp -= DEFAULT_KELVIN;
    }

    public double getTemp() {
        return this.mainDto.temp;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MainDto {

        @JsonProperty("temp")
        private double temp; // kelvin
    }
}
