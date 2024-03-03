package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMainDto {

    public static final double DEFAULT_KELVIN = 273.15;

    @JsonProperty("temperature")
    private double temp;

    public void convertToCelsius() {
        this.temp -= DEFAULT_KELVIN;
    }

    public void round() {
        this.temp=Math.round(this.temp);
    }

    public void ceil() {
        this.temp=Math.ceil(this.temp);
    }
}
