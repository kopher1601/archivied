package jp.co.topucomunity.backend_java.recruitments.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jp.co.topucomunity.backend_java.config.OAuth2UserPrincipal;
import jp.co.topucomunity.backend_java.users.domain.User;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TopuMockSecurityContext implements WithSecurityContextFactory<TopuMockUser> {

    @Value("${jwt.sign.key}")
    private String jwtSignKey;
    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(TopuMockUser annotation) {

        var savedUser = userRepository.save(User.of(UUID.randomUUID().toString(), annotation.email()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Map<String, Object> attributes = Map.of(
                annotation.nameAttributeKey(),
                annotation.username(),
                "email",
                annotation.email()
        );

        OAuth2User principal = new DefaultOAuth2User(
                AuthorityUtils.createAuthorityList(annotation.authorities()),
                attributes,
                annotation.nameAttributeKey()
        );

        var secretKey = Keys.hmacShaKeyFor(jwtSignKey.getBytes());

        var jws = Jwts.builder()
                .id(String.valueOf(savedUser.getUserId()))
                .subject(savedUser.getSub())
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();

        var oAuth2UserPrincipal = new OAuth2UserPrincipal(savedUser.getUserId(), jws, principal);

        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                oAuth2UserPrincipal,
                AuthorityUtils.createAuthorityList(annotation.authorities()),
                annotation.registrationId()
        );

        context.setAuthentication(authentication);
        return context;
    }
}
