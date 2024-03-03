package project.app.apns_server.modules.service.weather.weatherkit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.app.apns_server.modules.dto.WeatherKitHeaderDto;
import project.app.apns_server.modules.dto.WeatherKitPayloadDto;
import project.app.apns_server.modules.service.ObjectMapperService;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherKitJwtTokenizer {

    private static final String WEATHER_KEY_FILE = "app/AuthKey_FSWB6X72BA.p8";

    private final ObjectMapperService objectMapperService;

    private PrivateKey getPrivateKeyFromFile() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(WEATHER_KEY_FILE);
        String privateKeyContent = new String(fileInputStream.readAllBytes());
        privateKeyContent = privateKeyContent
                .replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(spec);
    }

    public String createJwtToken(String kId, String teamId, String serviceId)
            throws Exception {

        String jwtHeader = encodingByBase64(
                objectMapperService.serializeWeatherKitHeaderDto(
                        WeatherKitHeaderDto.of(kId, teamId, serviceId))
                        .getBytes(StandardCharsets.UTF_8));

        String jwtPayload = encodingByBase64(
                objectMapperService.serializeWeatherKitPayloadDto(
                        WeatherKitPayloadDto.of(teamId, getCurrTimeInSeconds(), getOneHourLaterTime() ,serviceId))
                        .getBytes(StandardCharsets.UTF_8));

        String unsignedToken = jwtHeader + "." + jwtPayload;
        byte[] signatureBytes = getSignatureBytes(unsignedToken);
        String jwtSignature = encodingByBase64(signatureBytes);
        String s = unsignedToken + "." + jwtSignature;
        log.info("jwt = {}",s);
        return s;
    }

    private byte[] getSignatureBytes(String unsignedToken) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA"); // es256
        signature.initSign(getPrivateKeyFromFile());
        signature.update(unsignedToken.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    private static String encodingByBase64(byte[] data) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(data);
    }

    private long getCurrTimeInSeconds() {
        return System.currentTimeMillis()/1000;
    }

    private long getOneHourLaterTime() {
        return getCurrTimeInSeconds() + 3600;
    }
}
