package project.app.apns_server.modules.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.dto.AppInfoRequestDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppInfoVo {
    private String deviceToken;
    private String pushToken;
    private double latitude;
    private double longitude;
    private long temp;

    private AppInfoVo(String deviceToken, String pushToken, double latitude, double longitude, long temp) {
        this.deviceToken = deviceToken;
        this.pushToken = pushToken;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
    }

    public static AppInfoVo of(AppInfoRequestDto appInfo, WeatherApiResponseDto weatherInfo) {
        return new AppInfoVo(appInfo.getDeviceToken(),
                appInfo.getPushToken(),
                appInfo.getLatitude(),
                appInfo.getLongitude(),
                (long) (weatherInfo.mainDto().getTemp())
        );
    }

    public static AppInfoVo ofApnsTest(String pushToken, long temp) {
        return AppInfoVo.builder()
                .pushToken(pushToken)
                .temp(temp)
                .build();
    }

    public static AppInfoVo of(AppInfoRequestDto appInfo) {
        return new AppInfoVo(
                appInfo.getDeviceToken(),
                appInfo.getPushToken(),
                appInfo.getLatitude(),
                appInfo.getLongitude(),
                appInfo.getTemp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfoVo appInfoVo)) return false;
        return Objects.equals(deviceToken, appInfoVo.deviceToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceToken);
    }

    public void updateCurrTemp(long currTemp) {
        this.temp = currTemp;
    }
}
