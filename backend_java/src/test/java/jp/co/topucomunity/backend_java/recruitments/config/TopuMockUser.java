package jp.co.topucomunity.backend_java.recruitments.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TopuMockSecurityContext.class)
public @interface TopuMockUser {

    String id() default "1";
    String username() default "falsystack";
    String email() default "falsystack@gmail.com";
    String[] authorities() default { "ROLE_USER" };
    String registrationId() default "google";
    String nameAttributeKey() default "sub";
    String clientId() default "test-client-id";
    String clientSecret() default "test-client-secret";

}