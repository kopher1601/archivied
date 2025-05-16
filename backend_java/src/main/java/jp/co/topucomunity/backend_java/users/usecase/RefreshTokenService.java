package jp.co.topucomunity.backend_java.users.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private final static Duration REFRESH_TOKEN_DURATION = Duration.ofDays(30);

    public void storeRefreshToken(Long userId, String refreshToken) {
        // TODO release 1.0 이후에 대응
        //redisTemplate.opsForValue().set("refreshToken:" + refreshToken, String.valueOf(userId), REFRESH_TOKEN_DURATION);
    }

    public Long getUserIdByRefreshToken(String refreshToken) {
        var userId = redisTemplate.opsForValue().get("refreshToken:" + refreshToken);
        if (userId != null) {
            return Long.parseLong(userId);
        }
        return null;
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete("refreshToken:" + refreshToken);
    }

}
