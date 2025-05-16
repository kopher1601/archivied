package jp.co.topucomunity.backend_java.users.usecase.in;

import jp.co.topucomunity.backend_java.users.controller.in.SignUpRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RegisterUser {
    private final Long userId;
    private final String nickname;
    private final String positionName;
    private final Integer personalHistoryYear;
    private final List<String> techStackNames;

    @Builder
    private RegisterUser(Long userId, String nickname, String positionName, Integer personalHistoryYear, List<String> techStackNames) {
        this.userId = userId;
        this.nickname = nickname;
        this.positionName = positionName;
        this.personalHistoryYear = personalHistoryYear;
        this.techStackNames = techStackNames;
    }

    public static RegisterUser of(Long id, SignUpRequest request) {
        return RegisterUser.builder()
                .userId(id)
                .nickname(request.getNickname())
                .positionName(request.getPositionName())
                .personalHistoryYear(request.getPersonalHistoryYear())
                .techStackNames(request.getTechStackNames())
                .build();
    }
}
