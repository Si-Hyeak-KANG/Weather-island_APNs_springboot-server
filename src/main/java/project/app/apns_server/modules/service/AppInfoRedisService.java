package project.app.apns_server.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.dto.AppInfoRequestDto;
import project.app.apns_server.modules.dto.WeatherApiResponseDto;
import project.app.apns_server.modules.vo.AppInfoVo;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppInfoRedisService {

    private static final String APP_INFO_KEY = "APP_INFO";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public AppInfoVo saveInfo(AppInfoRequestDto appInfo, WeatherApiResponseDto weatherInfo) {

        AppInfoVo info = AppInfoVo.of(appInfo, weatherInfo);
        try {
            hashOperations.put(APP_INFO_KEY,
                    info.getLiveActivityToken(),
                    serializeAppInfoVo(info));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return info;
    }

    private String serializeAppInfoVo(AppInfoVo info) throws JsonProcessingException {
        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(info);
    }

    private AppInfoVo deserializeAppInfoVo(String value) throws JsonProcessingException {
        return objectMapper.registerModule(new JavaTimeModule()).readValue(value, AppInfoVo.class);
    }
}