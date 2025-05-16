package jp.co.topucomunity.backend_java.users.application.service;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.users.application.port.in.PositionLookupPort;
import jp.co.topucomunity.backend_java.users.application.port.in.RegisterUserCommand;
import jp.co.topucomunity.backend_java.users.application.port.in.SignupUsecase;
import jp.co.topucomunity.backend_java.users.application.port.in.TechStacksLookupPort;
import jp.co.topucomunity.backend_java.users.application.port.out.SignupPort;
import jp.co.topucomunity.backend_java.users.application.port.out.SignupUser;
import jp.co.topucomunity.backend_java.users.exception.AlreadyExistNicknameException;
import jp.co.topucomunity.backend_java.users.exception.AlreadyExistTopuAuthException;
import jp.co.topucomunity.backend_java.users.exception.TopuAuthNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignupService implements SignupUsecase {

    private final SignupPort signupPort;
    private final PositionLookupPort positionLookupPort;
    private final TechStacksLookupPort techStacksLookupPort;

    @Transactional
    @Override
    public void signup(RegisterUserCommand command) {


        var foundUser = signupPort.findById(command.getUserId())
                .orElseThrow(TopuAuthNotFoundException::new);

        if (!foundUser.isFirstLogin()) {
            throw new AlreadyExistTopuAuthException();
        }

        signupPort.findByNickname(command.getNickname()).ifPresent(user -> {
            throw new AlreadyExistNicknameException(user.getNickname());
        });

        var position = positionLookupPort.findPositionByPositionName(command.getPositionName())
                .orElseGet(() -> Position.from(command.getPositionName()));

        var techStacks = command.getTechStackNames().stream().map(techStackName ->
                        techStacksLookupPort.findByTechnologyName(techStackName)
                                .orElseGet(() -> TechStack.from(techStackName)))
                .toList();


        foundUser.updateFirstLoginUser(SignupUser.builder()
                .userId(command.getUserId())
                .nickname(command.getNickname())
                .position(position)
                .personalHistoryYear(command.getPersonalHistoryYear())
                .techStacks(techStacks)
                .build());
    }
}
