package jp.co.topucomunity.backend_java.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Deprecated
@RequiredArgsConstructor
@Getter
public class OAuth2UserPrincipal implements OAuth2User {

    private final Long userId;
    private final String jws;
    private final OAuth2User oAuth2User;
    
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttributes().get("email").toString();
    }
}
