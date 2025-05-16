package jp.co.topucomunity.backend_java.users.exception;

import org.springframework.http.HttpStatus;

public class TopuAuthNotFoundException extends TopuAuthException {

    private static final String MESSAGE = "해당하는 유저가 존재하지 않습니다.";

    public TopuAuthNotFoundException() {
        super(MESSAGE);
    }

    public TopuAuthNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
