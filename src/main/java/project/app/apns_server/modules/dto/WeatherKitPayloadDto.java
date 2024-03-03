package project.app.apns_server.modules.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeatherKitPayloadDto {

    private String iss;
    private Long iat;
    private Long exp;
    private String sub;

    public static WeatherKitPayloadDto of(String iss,Long iat, Long exp, String sub) {
        return new WeatherKitPayloadDto(iss, iat, exp, sub);
    }
}
