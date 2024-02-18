package project.app.apns_server.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.CurrAppInfoDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppInfoRedisService {

    private static final String APP_INFO_KEY = "APP_INFO";
    private static final String WEATHER_KEY = "WEATHER_INFO";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void saveInfo(CurrAppInfoDto info) {
        try {
            hashOperations.put(APP_INFO_KEY,
                    info.getLiveActivityToken(),
                    serializeCurrAppInfoDto(info));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveWeather(CurrAppInfoDto appInfo, WeatherApiResponseDto weatherInfo) {
        try {
            hashOperations.put(WEATHER_KEY,
                    appInfo.getLiveActivityToken(),
                    serializeWeatherInfoDto(weatherInfo));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeCurrAppInfoDto(CurrAppInfoDto info) throws JsonProcessingException {
        return objectMapper.writeValueAsString(info);
    }

    private CurrAppInfoDto deserializeCurrAppInfoDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, CurrAppInfoDto.class);
    }

    private String serializeWeatherInfoDto(WeatherApiResponseDto info) throws JsonProcessingException {
        return objectMapper.writeValueAsString(info);
    }

    private WeatherApiResponseDto deserializeWeatherInfoDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, WeatherApiResponseDto.class);
    }
}
