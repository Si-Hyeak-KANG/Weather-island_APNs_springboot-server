package project.app.apns_server.modules.service.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.common.exception.exceptionCode.BusinessLogicException;
import project.app.apns_server.modules.common.exception.exceptionCode.ExceptionCode;
import project.app.apns_server.modules.service.ObjectMapperService;
import project.app.apns_server.modules.vo.AppInfoVo;

import java.util.Map;
import java.util.Optional;

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
        if(isUpdate) log.info("[AppInfoRedisServiceImpl saveInfo] (update) 기존에 있던 앱의 정보를 갱신했습니다.");
        else log.info("[AppInfoRedisServiceImpl saveInfo] (create) 새로운 앱의 정보를 캐시에 저장했습니다.");
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

    @Override
    public void deleteDeviceByPushToken(String pushToken) {
        String pushTokenRegex = ".*"+pushToken+".*";

        Optional<Map.Entry<String, String>> result = findDeviceByPushToken(pushTokenRegex);

        result.ifPresent(entry -> {
            hashOperations.delete(APP_INFO_KEY, entry.getKey());
            log.info("성공적으로 Push 토큰에 해당하는 디바이스 정보를 캐시에서 삭제했습니다.");
            log.info("삭제된 Device token : {}", entry.getKey());
            log.info("Matched Value : {}", entry.getValue());
        });

        if (result.isEmpty()) log.info("Push 토큰에 해당하는 디바이스가 없기 때문에 캐시에서 삭제를 하지 않습니다.");
    }

    private Optional<Map.Entry<String, String>> findDeviceByPushToken(String regex) {
        return hashOperations.entries(APP_INFO_KEY).entrySet().stream()
                .filter(entry -> entry.getValue().matches(regex))
                .findFirst();
    }

    private void validDeviceToken(String deviceToken) {
        if (!hashOperations.hasKey(APP_INFO_KEY, deviceToken)) {
            log.debug("[validDeviceToken] deviceToken : {}", deviceToken);
            throw new BusinessLogicException(ExceptionCode.RESOURCE_NOT_FOUND);
        }
    }

}
