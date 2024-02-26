package project.app.apns_server.modules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApnsTestRequestDto {

    @NotBlank(message = "Push 토큰 미입력")
    @JsonProperty("push_token")
    private String pushToken;

    @NotBlank(message = "APNs ID 미입력")
    @Pattern(regexp = "^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$",
            message = "Canonical UUIDs are 32 lowercase hexadecimal digits, displayed in five groups separated by hyphens in the form 8-4-4-4-12. For example: 123e4567-e89b-12d3-a456-4266554400a0. ")
    @JsonProperty("apns_id")
    private String apnsId;
}
