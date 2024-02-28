package project.app.apns_server.modules.common.exception.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    CLIENT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 정보로 요청하였습니다."),
    INVALID_VALIDATION(HttpStatus.BAD_REQUEST, "요청 데이터의 유효성 검증을 실패하였습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하는 내용과 일치하는 데이터가 없습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "데이터 제약조건 위반입니다."),

    INVALID_PUSH_TOKEN(HttpStatus.BAD_REQUEST, "입력한 토큰 정보가 잘못되었습니다."),
    APNS_PUSH_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "요청한 Push 토큰은 이미 만료되었습니다."),
    APNS_PUSH_NOTIFICATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Push Notification 전송을 실패하였습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 측에서 발생한 에러입니다. 서버에 문의해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
