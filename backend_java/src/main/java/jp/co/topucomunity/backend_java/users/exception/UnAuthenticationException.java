package jp.co.topucomunity.backend_java.users.exception;

import org.springframework.http.HttpStatus;

public class UnAuthenticationException extends TopuAuthException {

    private static final String MESSAGE = "인증에 실패 했습니다.";

    public UnAuthenticationException() {
        super(MESSAGE);
    }

    public UnAuthenticationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
