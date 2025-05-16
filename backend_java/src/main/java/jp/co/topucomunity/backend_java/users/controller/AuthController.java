package jp.co.topucomunity.backend_java.users.controller;

import jp.co.topucomunity.backend_java.users.controller.out.UserResponse;
import jp.co.topucomunity.backend_java.users.domain.UserSession;
import jp.co.topucomunity.backend_java.users.exception.UnAuthenticationException;
import jp.co.topucomunity.backend_java.users.usecase.RefreshTokenService;
import jp.co.topucomunity.backend_java.users.usecase.UserUsecase;
import jp.co.topucomunity.backend_java.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserUsecase userUsecase;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userUsecase.getUser(userId);
    }

    @GetMapping("/watashi")
    public UserResponse me(UserSession userSession) {
        return userUsecase.getUser(Long.valueOf(userSession.id()));
    }

    // TODO : release 1.0 이후 사용
    // @PostMapping("/refresh")
    public ResponseEntity<?> refresh(UserSession userSession, @RequestBody Map<String, String> request) {
        var refreshToken = request.get("refreshToken");

        var userId = refreshTokenService.getUserIdByRefreshToken(refreshToken);
        if (userId == null) {
            throw new UnAuthenticationException();
        }

        var newJwtToken = jwtUtil.generateToken(userId);

        var responseData = new HashMap<String, String>();
        responseData.put("token", newJwtToken);

        return ResponseEntity.ok(responseData);
    }
}
