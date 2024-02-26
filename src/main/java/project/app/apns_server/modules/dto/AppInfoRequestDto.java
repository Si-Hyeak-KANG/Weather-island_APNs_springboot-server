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
 * @Param :  Device token, LiveActivity token, 위도(Latitude), 경도(longitude)
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

    @NotBlank(message = "APNs ID 미입력")
    @Pattern(regexp = "^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$",
            message = "Canonical UUIDs are 32 lowercase hexadecimal digits, displayed in five groups separated by hyphens in the form 8-4-4-4-12. For example: 123e4567-e89b-12d3-a456-4266554400a0. ")
    @JsonProperty("apns_id")
    private String apnsId;

    @JsonProperty("lat")
    @NotNull(message = "위도(lat) 미입력")
    private Double latitude;

    @JsonProperty("lon")
    @NotNull(message = "경도(lon) 미입력")
    private Double longitude;
}
