package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * LiveActivity 를 실행한 앱의 토큰과 위치 정보
 * @Param :  Device token, LiveActivity token, 위도(Latitude), 경도(longitude)
 */
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoRequestDto {

    @JsonProperty("device_token")
    private String deviceToken;

    @JsonProperty("push_token")
    private String pushToken;

    @JsonProperty("apns_id")
    private String apnsId;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;
}
