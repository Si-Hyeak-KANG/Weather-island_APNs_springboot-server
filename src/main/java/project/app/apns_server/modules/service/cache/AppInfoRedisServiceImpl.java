package project.app.apns_server.modules.service.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.service.ObjectMapperService;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppInfoRedisServiceImpl implements AppInfoRedisService {

    private static final String APP_INFO_KEY = "APP_INFO";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapperService objectMapperService;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public boolean saveInfo(final AppInfoVo info){
        boolean isUpdate = isAlreadyExistField(info.getDeviceToken());
        hashOperations.put(APP_INFO_KEY, info.getDeviceToken(), objectMapperService.serializeAppInfoVo(info));
        if(isUpdate) log.info("[AppInfoRedisServiceImpl saveInfo] (Device token= {}) 기존에 있던 앱의 정보를 갱신했습니다.", info.getDeviceToken());
        else log.info("[AppInfoRedisServiceImpl saveInfo] (Device token= {}) 앱의 정보를 캐시에 저장했습니다.", info.getDeviceToken());
        return isUpdate;
    }

    private boolean isAlreadyExistField(String deviceToken) {
        return hashOperations.hasKey(APP_INFO_KEY, deviceToken);
    }

    @Override
    public AppInfoVo findAppInfoByDeviceToken(String deviceToken) {
        validDeviceToken(deviceToken);
        String data = hashOperations.entries(APP_INFO_KEY).get(deviceToken);
        log.debug("[AppInfoRedisServiceImpl findAppInfoByDeviceToken] (Device token={}) {}", deviceToken, data);
        return objectMapperService.deserializeAppInfoVo(data);
    }

    private void validDeviceToken(String deviceToken) {
        if (!hashOperations.hasKey(APP_INFO_KEY, deviceToken)) {
            throw new NoSuchElementException(deviceToken + " 에 해당하는 데이터를 찾을 수 없습니다.");
        }
    }

}
