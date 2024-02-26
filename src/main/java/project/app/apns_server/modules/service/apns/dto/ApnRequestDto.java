package project.app.apns_server.modules.service.apns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApnRequestDto {

    @JsonProperty("aps")
    private Aps aps;

    public static ApnRequestDto of(long timestamp, String event, Map<String, Object> contentState) {
        return new ApnRequestDto(
                Aps.builder()
                        .timestamp(timestamp)
                        .event(event)
                        .contentState(contentState)
                        .build());
    }

    @Builder
    protected static class Aps {
        @JsonProperty("timestamp")
        private long timestamp;

        @JsonProperty("event")
        private String event;

        @JsonProperty("content-state")
        private Map<String, Object> contentState;
    }
}
