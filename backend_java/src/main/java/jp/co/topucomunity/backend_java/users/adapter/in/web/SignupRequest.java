package jp.co.topucomunity.backend_java.users.adapter.in.web;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequest {

    private final String nickname;
    private final String positionName;
    private final Integer personalHistoryYear;
    private final List<String> techStackNames;

    @Builder
    private SignupRequest(String nickname, String positionName, Integer personalHistoryYear, List<String> techStackNames) {
        this.nickname = nickname;
        this.positionName = positionName;
        this.personalHistoryYear = personalHistoryYear;
        this.techStackNames = techStackNames;
    }

    public static SignupRequest createSignUpRequest(String nickname, String positionName, Integer personalHistoryYear, List<String> techStackNames) {
        return SignupRequest.builder()
                .nickname(nickname)
                .positionName(positionName)
                .personalHistoryYear(personalHistoryYear)
                .techStackNames(techStackNames)
                .build();
    }
}
