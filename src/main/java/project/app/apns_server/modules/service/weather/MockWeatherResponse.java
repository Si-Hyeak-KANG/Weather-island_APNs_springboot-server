package project.app.apns_server.modules.service.weather;

import project.app.apns_server.modules.dto.WeatherApiResponseDto;

public class MockWeatherResponse extends WeatherApiResponseDto {

    public MockWeatherResponse() {
        super(new MainDto(280.08));
    }
}
