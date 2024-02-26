package project.app.apns_server.modules.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.app.apns_server.modules.common.dto.Response;
import project.app.apns_server.modules.common.exception.exceptionCode.ExceptionCode;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<Response> handleValidException(Exception e) {

        ExceptionCode error = ExceptionCode.CLIENT_BAD_REQUEST;
        log.error("[{}] {}", error.name(), error.getMessage());
        log.error("error reason : {}", e.getMessage());
        ErrorsResponse response = ErrorsResponse.of(error);
        response.addReason(e.getMessage());
        return new ResponseEntity<>(Response.fail(response), error.getHttpStatus());
    }

    /**
     * 유효성 검증 에러 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidException(MethodArgumentNotValidException e) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<ErrorsResponse.ValidationError> validResult =
                fieldErrors.stream()
                        .map(ErrorsResponse.ValidationError::of)
                        .toList();

        ExceptionCode error = ExceptionCode.INVALID_VALIDATION;
        log.error("[{}] {}", error.name(), error.getMessage());
        log.error("error reason : {}" , e.getMessage());
        ErrorsResponse response = ErrorsResponse.of(error, validResult);
        return new ResponseEntity<>(Response.fail(response), error.getHttpStatus());
    }
}
