package project.app.apns_server.modules.common.exception.exceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessLogicException extends RuntimeException {
    private ExceptionCode exceptionCode;

    private String errorMessage;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public BusinessLogicException(ExceptionCode exceptionCode, String errorMessage) {
        this.exceptionCode = exceptionCode;
        this.errorMessage = errorMessage;
    }


}
