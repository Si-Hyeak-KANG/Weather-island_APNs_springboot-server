package project.app.apns_server.modules.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.FieldError;
import project.app.apns_server.modules.common.exception.exceptionCode.ExceptionCode;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorsResponse {

    private final int code;
    private final String message;
    private String reason;

    private final List<ValidationError> validationError;

    public static ErrorsResponse of(final ExceptionCode e) {
        return ErrorsResponse.builder()
                .code(e.getHttpStatus().value())
                .message(e.getMessage())
                .build();
    }

    public static ErrorsResponse of(final ExceptionCode e, List<ValidationError> validationErrors) {
        return ErrorsResponse.builder()
                .code(e.getHttpStatus().value())
                .message(e.getMessage())
                .validationError(validationErrors)
                .build();
    }

    public void addReason(String reason) {
        this.reason = reason;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private Object rejectedValue;
        private String reason;

        public static ValidationError of(final FieldError e) {
            return ValidationError.builder()
                    .field(e.getField())
                    .rejectedValue(e.getRejectedValue())
                    .reason(e.getDefaultMessage())
                    .build();
        }
    }
}