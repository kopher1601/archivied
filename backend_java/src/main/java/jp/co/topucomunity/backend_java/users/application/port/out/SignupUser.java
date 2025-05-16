package jp.co.topucomunity.backend_java.users.application.port.out;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class SignupUser {
    private final Long userId;
    private final String nickname;
    private final Position position;
    private final Integer personalHistoryYear;
    private final List<TechStack> techStacks;

    @Builder
    private SignupUser(Long userId, String nickname, Position position, Integer personalHistoryYear, List<TechStack> techStacks) {
        this.userId = userId;
        this.nickname = nickname;
        this.position = position;
        this.personalHistoryYear = personalHistoryYear;
        this.techStacks = techStacks;
    }


}
