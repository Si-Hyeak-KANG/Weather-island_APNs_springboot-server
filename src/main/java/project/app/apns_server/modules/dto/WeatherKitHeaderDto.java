package project.app.apns_server.modules.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeatherKitHeaderDto {

    private String alg;
    private String kid;
    private String id;

    public static WeatherKitHeaderDto of(String kid, String teamId, String serviceId) {
        return new WeatherKitHeaderDto("ES256", kid, teamId+"."+serviceId);
    }
}
