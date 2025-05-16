package jp.co.topucomunity.backend_java.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jp.co.topucomunity.backend_java.users.exception.UnAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.sign.key}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public Jws<Claims> verifyAndParseToken(String token) {
        try {
            var claimsJws = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token);

            if (!claimsJws.getPayload().getExpiration().after(new Date())) {
                throw new UnAuthenticationException();
            }

            return claimsJws;
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnAuthenticationException(e);
        }
    }
}
