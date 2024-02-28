package project.app.apns_server.modules.service.cache;

import project.app.apns_server.modules.vo.AppInfoVo;

public interface AppInfoRedisService {

    boolean saveInfo(final AppInfoVo info);

    AppInfoVo findAppInfoByDeviceToken(String deviceToken);

    void deleteDeviceByPushToken(String pushToken);
}
