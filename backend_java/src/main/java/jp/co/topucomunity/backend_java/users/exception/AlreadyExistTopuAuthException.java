package jp.co.topucomunity.backend_java.users.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistTopuAuthException extends TopuAuthException {

    private static final String MESSAGE = "이미 등록된 유저입니다.";

    public AlreadyExistTopuAuthException() {
        super(MESSAGE);
    }

    public AlreadyExistTopuAuthException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
