package project.app.apns_server.modules.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.dto.AppInfoRequestDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppInfoVo {
    private String deviceToken;
    private String pushToken;
    private String apnsId;
    private double latitude;
    private double longitude;
    private long temp;

    private AppInfoVo(String deviceToken, String pushToken, String apnsId, double latitude, double longitude, long temp) {
        this.deviceToken = deviceToken;
        this.pushToken = pushToken;
        this.apnsId = apnsId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
    }

    public static AppInfoVo of(AppInfoRequestDto appInfo, WeatherApiResponseDto weatherInfo) {
        return new AppInfoVo(appInfo.getDeviceToken(),
                appInfo.getPushToken(),
                appInfo.getApnsId(),
                appInfo.getLatitude(),
                appInfo.getLongitude(),
                weatherInfo.getTemp()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfoVo appInfoVo)) return false;

        return apnsId.equals(appInfoVo.apnsId);
    }

    @Override
    public int hashCode() {
        return apnsId.hashCode();
    }

    public void updateCurrTemp(long currTemp) {
        this.temp = currTemp;
    }
}
