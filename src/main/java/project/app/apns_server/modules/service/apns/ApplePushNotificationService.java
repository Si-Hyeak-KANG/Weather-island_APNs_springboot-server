package project.app.apns_server.modules.service.apns;


public interface ApplePushNotificationService {
    void pushNotification(String pushToken, String apnsId, String temp);
}
