package jp.co.topucomunity.backend_java.recruitments.controller.out;

import jp.co.topucomunity.backend_java.recruitments.domain.Recruitment;
import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentPosition;
import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentTechStack;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RecruitmentResponse {
    private final Long id;
    private final Long userId;
    private final String nickname;
    private final RecruitmentCategories recruitmentCategories;
    private final ProgressMethods progressMethods;
    private final int numberOfPeople;
    private final int progressPeriod;
    private final LocalDate recruitmentDeadline;
    private final String contract;
    private final String subject;
    private final String content;
    private final List<String> techStacks;
    private final List<String> positions;

    @Builder
    private RecruitmentResponse(Long id, Long userId, String nickname, RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, int numberOfPeople, int progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, List<String> techStacks, List<String> positions) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.recruitmentCategories = recruitmentCategories;
        this.progressMethods = progressMethods;
        this.numberOfPeople = numberOfPeople;
        this.progressPeriod = progressPeriod;
        this.recruitmentDeadline = recruitmentDeadline;
        this.contract = contract;
        this.subject = subject;
        this.content = content;
        this.techStacks = techStacks;
        this.positions = positions;
    }

    public static RecruitmentResponse from(Recruitment recruitment) {
        return RecruitmentResponse.builder()
                .id(recruitment.getId())
                .userId(recruitment.getRecruitmentUser().getUser().getUserId())
                .nickname(recruitment.getRecruitmentUser().getUser().getNickname())
                .recruitmentCategories(recruitment.getRecruitmentCategories())
                .progressMethods(recruitment.getProgressMethods())
                .numberOfPeople(recruitment.getNumberOfPeople())
                .progressPeriod(recruitment.getProgressPeriod())
                .recruitmentDeadline(recruitment.getRecruitmentDeadline())
                .contract(recruitment.getContract())
                .subject(recruitment.getSubject())
                .content(recruitment.getContent())
                .techStacks(recruitment.getRecruitmentTechStacks().stream()
                        .map(RecruitmentTechStack::getTechnologyName).toList())
                .positions(recruitment.getRecruitmentPositions().stream()
                        .map(RecruitmentPosition::getPositionName).toList())
                .build();
    }
}
