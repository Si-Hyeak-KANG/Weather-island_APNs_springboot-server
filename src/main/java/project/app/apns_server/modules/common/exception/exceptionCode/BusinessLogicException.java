package project.app.apns_server.modules.common.exception.exceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BusinessLogicException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private String errorMessage;
}
