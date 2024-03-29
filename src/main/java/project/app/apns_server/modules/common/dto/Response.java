package project.app.apns_server.modules.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response {

    private enum Status {success, fail}
    private Status status;

    @JsonProperty("res_dt")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime responseTime;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private Object errors;

    @JsonProperty("app")
    private AppResponse appResponse;

    @JsonProperty("weather")
    private WeatherResponse weatherResponse;

    public static Response success(AppInfoVo result) {
        AppResponse app = new AppResponse(result);
        WeatherResponse weather = new WeatherResponse(result);
        return Response.builder()
                .status(Status.success)
                .responseTime(LocalDateTime.now())
                .appResponse(app)
                .weatherResponse(weather)
                .build();
    }

    public static Response success(String msg) {
        return Response.builder()
                .status(Status.success)
                .responseTime(LocalDateTime.now())
                .message(msg)
                .build();
    }

    public static Response fail(Object err) {
        return Response.builder()
                .status(Status.fail)
                .responseTime(LocalDateTime.now())
                .errors(err)
                .build();
    }

    private static class AppResponse {

        @JsonProperty("device_token")
        private String deviceToken;

        @JsonProperty("push_token")
        private String pushToken;

        @JsonProperty("lat")
        private double latitude;

        @JsonProperty("lon")
        private double longitude;

        public AppResponse(AppInfoVo result) {
            this.deviceToken = result.getDeviceToken();
            this.pushToken = result.getPushToken();
            this.latitude = result.getLatitude();
            this.longitude = result.getLongitude();
        }
    }

    private static class WeatherResponse {

        @JsonProperty("temperature")
        private long temp;

        public WeatherResponse(AppInfoVo result) {
            this.temp = result.getTemp();
        }

    }

}
