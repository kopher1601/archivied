package jp.co.topucomunity.backend_java.recruitments.usecase.in;

import jp.co.topucomunity.backend_java.recruitments.controller.in.CreateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PostRecruitment {

    private Long userId;
    private RecruitmentCategories recruitmentCategories;
    private ProgressMethods progressMethods;
    private List<String> techStacks;
    private List<String> recruitmentPositions;
    private Integer numberOfPeople;
    private Integer progressPeriod;
    private LocalDate recruitmentDeadline;
    private String contract;
    private String subject;
    private String content;

    public static PostRecruitment of(CreateRecruitmentRequest request, Long userId) {
        return PostRecruitment.builder()
                .userId(userId)
                .recruitmentCategories(request.getRecruitmentCategories())
                .progressMethods(request.getProgressMethods())
                .techStacks(request.getTechStacks())
                .recruitmentPositions(request.getRecruitmentPositions())
                .numberOfPeople(request.getNumberOfPeople())
                .progressPeriod(request.getProgressPeriod())
                .recruitmentDeadline(request.getRecruitmentDeadline())
                .contract(request.getContract())
                .subject(request.getSubject())
                .content(request.getContent())
                .build();
    }
}
