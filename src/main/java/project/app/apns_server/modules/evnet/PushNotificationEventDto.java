package project.app.apns_server.modules.evnet;

public record PushNotificationEventDto(String pushToken, String apnsId, long temp) {

    public static PushNotificationEventDto of(String pushToken, String apnsId, long temp) {
        return new PushNotificationEventDto(pushToken, apnsId, temp);
    }
}
