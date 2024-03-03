package project.app.apns_server.modules.evnet;

import project.app.apns_server.modules.vo.AppInfoVo;

public record PushNotificationEventDto(AppInfoVo appInfoVo) {

    public static PushNotificationEventDto of(AppInfoVo appInfoVo) {
        return new PushNotificationEventDto(appInfoVo);
    }
}
