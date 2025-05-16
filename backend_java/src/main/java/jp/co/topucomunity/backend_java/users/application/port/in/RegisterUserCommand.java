package jp.co.topucomunity.backend_java.users.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RegisterUserCommand {
    private final Long userId;
    private final String nickname;
    private final String positionName;
    private final Integer personalHistoryYear;
    private final List<String> techStackNames;

    @Builder
    public RegisterUserCommand(Long userId, String nickname, String positionName, Integer personalHistoryYear, List<String> techStackNames) {
        this.userId = userId;
        this.nickname = nickname;
        this.positionName = positionName;
        this.personalHistoryYear = personalHistoryYear;
        this.techStackNames = techStackNames;
    }
}
