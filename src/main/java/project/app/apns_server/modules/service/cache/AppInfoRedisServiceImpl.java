package project.app.apns_server.modules.service.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.app.apns_server.modules.service.ObjectMapperService;
import project.app.apns_server.modules.vo.AppInfoVo;

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
        hashOperations.delete(APP_INFO_KEY);
    }

    @Override
    public boolean saveInfo(final AppInfoVo info) {
        boolean isUpdate = isExistFieldByDeviceToken(info.getDeviceToken());
        hashOperations.put(APP_INFO_KEY, info.getDeviceToken(), objectMapperService.serializeAppInfoVo(info));
        if (isUpdate) log.info("[saveInfo] (update) 기존에 있던 앱의 정보를 갱신했습니다.");
        else log.info("[saveInfo] (create) 새로운 앱의 정보를 캐시에 저장했습니다.");
        return isUpdate;
    }

    private boolean isExistFieldByDeviceToken(String deviceToken) {
        return hashOperations.hasKey(APP_INFO_KEY, deviceToken);
    }

    @Override
    public AppInfoVo findAppInfoByDeviceToken(String deviceToken) {
        if(isExistFieldByDeviceToken(deviceToken)) {
            String data = hashOperations.entries(APP_INFO_KEY).get(deviceToken);
            log.debug("[findAppInfoByDeviceToken] (Device token={}) {}", deviceToken, data);
            return objectMapperService.deserializeAppInfoVo(data);
        }
        log.info("[findAppInfoByDeviceToken] (Device token={}) device 토큰에 해당하는 정보가 없습니다.", deviceToken);
        return null;
    }

    @Override
    public void deleteAppInfoByDeviceToken(String deviceToken) {
        if (isExistFieldByDeviceToken(deviceToken)) {
            hashOperations.delete(APP_INFO_KEY, deviceToken);
            log.info("[deleteDeviceByPushToken] Push 토큰에 해당하는 데이터를 성공적으로 삭제했습니다.");
            log.info("[deleteDeviceByPushToken] 삭제된 Device token : {}", deviceToken);
        }
        else log.info("[deleteDeviceByPushToken] 성공적으로 Push 토큰에 해당하는 디바이스 정보를 캐시에서 삭제했습니다.");
    }

}
