package jp.co.topucomunity.backend_java.users.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistNicknameException extends TopuAuthException {

    private static final String MESSAGE = "는 이미 등록된 닉네임입니다.";

    public AlreadyExistNicknameException(String nickname) {
        super(String.format("%s %s",nickname, MESSAGE));
    }

    public AlreadyExistNicknameException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
