package project.app.apns_server.modules.service.apns;


import project.app.apns_server.modules.vo.AppInfoVo;

public interface ApplePushNotificationService {
    void pushNotification(AppInfoVo appInfoVo);
}
