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
    private String liveActivityToken;
    private double latitude;
    private double longitude;
    private double temp;

    private AppInfoVo(String deviceToken, String liveActivityToken, double latitude, double longitude,double temp) {
        this.deviceToken = deviceToken;
        this.liveActivityToken = liveActivityToken;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
    }

    public static AppInfoVo of(AppInfoRequestDto appInfo, WeatherApiResponseDto weatherInfo) {
        return new AppInfoVo(appInfo.getDeviceToken(),
                appInfo.getLiveActivityToken(),
                appInfo.getLatitude(),
                appInfo.getLongitude(),
                weatherInfo.getTemp()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfoVo appInfoVo)) return false;

        return liveActivityToken.equals(appInfoVo.liveActivityToken);
    }

    @Override
    public int hashCode() {
        return liveActivityToken.hashCode();
    }

    public void updateCurrTemp(double currTemp) {
        this.temp = currTemp;
    }
}
