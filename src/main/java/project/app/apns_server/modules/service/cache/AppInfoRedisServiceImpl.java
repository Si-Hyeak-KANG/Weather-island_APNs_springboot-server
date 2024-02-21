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

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppInfoRedisServiceImpl implements AppInfoRedisService {

    private static final String APP_INFO_KEY = "APP_INFO";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public boolean saveInfo(final AppInfoVo info){
        boolean isUpdate = isAlreadyExistField(info.getDeviceToken());
        hashOperations.put(APP_INFO_KEY, info.getDeviceToken(), serializeAppInfoVo(info));
        return isUpdate;
    }

    private boolean isAlreadyExistField(String deviceToken) {
        return hashOperations.hasKey(APP_INFO_KEY, deviceToken);
    }

    @Override
    public AppInfoVo findAppInfoByDeviceToken(String deviceToken) {
        validDeviceToken(deviceToken);
        String data = hashOperations.entries(APP_INFO_KEY).get(deviceToken);
        return deserializeAppInfoVo(data);
    }

    private void validDeviceToken(String deviceToken) {
        if (!hashOperations.hasKey(APP_INFO_KEY, deviceToken)) {
            throw new NoSuchElementException(deviceToken + " 에 해당하는 데이터를 찾을 수 없습니다.");
        }
    }



    private String serializeAppInfoVo(AppInfoVo info){
        try {
            return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(info);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private AppInfoVo deserializeAppInfoVo(String value){
        try {
            return objectMapper.registerModule(new JavaTimeModule()).readValue(value, AppInfoVo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
