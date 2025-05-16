package jp.co.topucomunity.backend_java.users.exception;

public abstract class TopuAuthException extends RuntimeException {

    public TopuAuthException() {
    }

    public TopuAuthException(String message) {
        super(message);
    }

    public TopuAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
