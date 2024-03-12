package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * LiveActivity 를 실행한 앱의 토큰과 위치 정보
 * @Param :  Device token, LiveActivity token, 위도(Latitude), 경도(longitude), 온도(temperature)
 */
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoRequestDto {

    @NotBlank(message = "Device 토큰 미입력")
    @JsonProperty("device_token")
    private String deviceToken;

    @NotBlank(message = "Push 토큰 미입력")
    @JsonProperty("push_token")
    private String pushToken;

    @JsonProperty("lat")
    @NotNull(message = "위도(lat) 미입력")
    private Double latitude;

    @JsonProperty("lon")
    @NotNull(message = "경도(lon) 미입력")
    private Double longitude;

    @JsonProperty("temperature")
    private long temp;
}
