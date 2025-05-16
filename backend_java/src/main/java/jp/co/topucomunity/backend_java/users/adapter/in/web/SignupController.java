package jp.co.topucomunity.backend_java.users.adapter.in.web;

import jp.co.topucomunity.backend_java.users.application.port.in.RegisterUserCommand;
import jp.co.topucomunity.backend_java.users.application.port.in.SignupUsecase;
import jp.co.topucomunity.backend_java.users.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class SignupController {

    private final SignupUsecase signupUsecase;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(UserSession userSession, @RequestBody SignupRequest request) {
        signupUsecase.signup(RegisterUserCommand.builder()
                .userId(Long.valueOf(userSession.id()))
                .nickname(request.getNickname())
                .positionName(request.getPositionName())
                .personalHistoryYear(request.getPersonalHistoryYear())
                .techStackNames(request.getTechStackNames())
                .build());
    }
}
