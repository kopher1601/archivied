package jp.co.topucomunity.backend_java.users.controller;

import jp.co.topucomunity.backend_java.users.controller.out.UserErrorResponse;
import jp.co.topucomunity.backend_java.users.exception.TopuAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UsersExceptionController {

    @Value("${topu.cookie.name}")
    private String topuCookieName;

    @ExceptionHandler(TopuAuthException.class)
    public ResponseEntity<UserErrorResponse> usersExceptionHandler(TopuAuthException e) {
        log.error("[UserException] : {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .header(HttpHeaders.SET_COOKIE, disableCookie().toString())
                .body(UserErrorResponse.from(e.getMessage()));
    }

    private ResponseCookie disableCookie() {
        return ResponseCookie.from(topuCookieName, null)
                .path("/")
                .maxAge(-1)
                .sameSite(Cookie.SameSite.STRICT.name())
                .build();
    }
}
