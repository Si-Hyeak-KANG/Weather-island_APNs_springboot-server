package project.app.apns_server.modules.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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

    public String saveInfo(AppInfoVo info) throws JsonProcessingException {

        String returnValue = "";
        if (alreadyExistKey(info)) returnValue = "update";
        else returnValue = "create";

        hashOperations.put(APP_INFO_KEY,
                info.getDeviceToken(),
                serializeAppInfoVo(info));

        return returnValue;
    }

    private Boolean alreadyExistKey(AppInfoVo info) {
        return hashOperations.hasKey(APP_INFO_KEY, info.getDeviceToken());
    }

    public AppInfoVo findAppInfoByDeviceToken(String deviceToken) {
        try {
            String data = hashOperations.entries(APP_INFO_KEY).get(deviceToken);
            return deserializeAppInfoVo(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeAppInfoVo(AppInfoVo info) throws JsonProcessingException {
        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(info);
    }

    private AppInfoVo deserializeAppInfoVo(String value) throws JsonProcessingException {
        return objectMapper.registerModule(new JavaTimeModule()).readValue(value, AppInfoVo.class);
    }

}
