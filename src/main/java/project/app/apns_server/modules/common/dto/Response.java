package project.app.apns_server.modules.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response {

    private enum Status {success, fail}
    private Status status;

    @JsonProperty("res_dt")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime responseTime;

    @JsonProperty("app")
    private AppResponse appResponse;

    @JsonProperty("weather")
    private WeatherResponse weatherResponse;

    public static Response success(AppInfoVo result) {
        AppResponse app = new AppResponse(result);
        WeatherResponse weather = new WeatherResponse(result);
        return new Response(Status.success,LocalDateTime.now(),app,weather);
    }

    private static class AppResponse {

        @JsonProperty("device_token")
        private String deviceToken;

        @JsonProperty("live_activity_token")
        private String liveActivityToken;

        @JsonProperty("lat")
        private double latitude;

        @JsonProperty("lon")
        private double longitude;

        public AppResponse(AppInfoVo result) {
            this.deviceToken = result.getDeviceToken();
            this.liveActivityToken = result.getLiveActivityToken();
            this.latitude = result.getLatitude();
            this.longitude = result.getLongitude();
        }
    }

    private static class WeatherResponse {

        @JsonProperty("temperature")
        private double temp;

        public WeatherResponse(AppInfoVo result) {
            this.temp = result.getTemp();
        }

    }

}
