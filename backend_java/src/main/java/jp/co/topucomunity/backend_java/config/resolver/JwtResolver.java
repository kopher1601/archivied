package jp.co.topucomunity.backend_java.config.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jp.co.topucomunity.backend_java.users.domain.UserSession;
import jp.co.topucomunity.backend_java.users.exception.UnAuthenticationException;
import jp.co.topucomunity.backend_java.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtResolver implements HandlerMethodArgumentResolver {

    @Value("${topu.cookie.name}")
    private String topuCookieName;

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // convert to http servlet request
        var httpServletRequest =
                webRequest.getNativeRequest(HttpServletRequest.class);
        if (Objects.isNull(httpServletRequest)) {
            throw new IllegalClassFormatException();
        }

        var sessionCookie = findCookieByName(httpServletRequest.getCookies(), topuCookieName);

        var claimsJws = jwtUtil.verifyAndParseToken(sessionCookie.getValue());

        return new UserSession(claimsJws.getPayload().getSubject());
    }

    private static Cookie findCookieByName(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElseThrow(UnAuthenticationException::new);
    }
}
