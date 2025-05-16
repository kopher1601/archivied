package jp.co.topucomunity.backend_java.users.controller.out;

import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.users.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class UserResponse {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String position;
    private final List<String> techStacks;
    private final String affiliation;
    private final Boolean isPublicAffiliation;
    private final Integer personalHistoryYear;
    private final String selfIntroduction;
    private final List<String> links;
    private final boolean isFirstLogin;

    @Builder
    private UserResponse(Integer personalHistoryYear, Long userId, String email, String nickname, String position, List<String> techStacks, String affiliation, Boolean isPublicAffiliation, String selfIntroduction, List<String> links, boolean isFirstLogin) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.position = position;
        this.techStacks = techStacks;
        this.affiliation = affiliation;
        this.isPublicAffiliation = isPublicAffiliation;
        this.personalHistoryYear = personalHistoryYear;
        this.selfIntroduction = selfIntroduction;
        this.links = links;
        this.isFirstLogin = isFirstLogin;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .position(user.getPosition() == null ? "" : user.getPosition().getPositionName())
                .techStacks(user.getTechStacks() == null ? List.of() : user.getTechStacks().stream().map(TechStack::getTechnologyName).toList())
                .affiliation(user.getAffiliation())
                .isPublicAffiliation(user.isPublicAffiliation())
                .personalHistoryYear(user.getPersonalHistoryYear())
                .selfIntroduction(user.getSelfIntroduction())
                .links(user.getLinks() == null || user.getLinks().isBlank() ? List.of() : Arrays.stream(user.getLinks().split(",")).map(String::trim).toList())
                .isFirstLogin(user.isFirstLogin())
                .build();
    }
}
