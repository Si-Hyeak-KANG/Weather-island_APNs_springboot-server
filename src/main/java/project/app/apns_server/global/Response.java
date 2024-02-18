package project.app.apns_server.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.dto.CurrAppInfoDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response {

    @JsonProperty("app_info")
    private CurrAppInfoDto appInfo;

    @JsonProperty("weather_info")
    private WeatherApiResponseDto weatherInfo;

    public static Response from(CurrAppInfoDto appInfo, WeatherApiResponseDto weatherInfo) {
        return Response.builder()
                .appInfo(appInfo)
                .weatherInfo(weatherInfo)
                .build();
    }

}
