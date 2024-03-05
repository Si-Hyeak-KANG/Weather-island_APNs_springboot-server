package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.app.apns_server.modules.vo.AppInfoVo;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApnsTestRequestDto {

    @NotBlank(message = "Push 토큰 미입력")
    @JsonProperty("push_token")
    private String pushToken;

    public AppInfoVo toVo(long temp) {
        return AppInfoVo.ofApnsTest(this.pushToken, temp);
    }
}
