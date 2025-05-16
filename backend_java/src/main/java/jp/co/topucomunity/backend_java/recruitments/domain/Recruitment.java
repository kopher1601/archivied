package jp.co.topucomunity.backend_java.recruitments.domain;

import jakarta.persistence.*;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.PostRecruitment;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.UpdateRecruitment;
import jp.co.topucomunity.backend_java.users.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitmentCategories recruitmentCategories;

    @Enumerated(EnumType.STRING)
    private ProgressMethods progressMethods;

    private Integer numberOfPeople;
    private Integer progressPeriod;
    private LocalDate recruitmentDeadline;
    private String contract;
    private String subject;
    private Long views = 0L;

    @Lob
    private String content;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();

    @OneToOne(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private RecruitmentUser recruitmentUser;

    @Builder
    private Recruitment(RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, int numberOfPeople, int progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, List<RecruitmentTechStack> recruitmentTechStacks, RecruitmentUser recruitmentUser) {
        this.recruitmentCategories = recruitmentCategories;
        this.progressMethods = progressMethods;
        this.numberOfPeople = numberOfPeople;
        this.progressPeriod = progressPeriod;
        this.recruitmentDeadline = recruitmentDeadline;
        this.contract = contract;
        this.subject = subject;
        this.content = content;
        this.recruitmentTechStacks = recruitmentTechStacks;
        this.recruitmentUser = recruitmentUser;
    }

    public static Recruitment from(PostRecruitment postRecruitment) {
        return Recruitment.builder()
                .recruitmentCategories(postRecruitment.getRecruitmentCategories())
                .progressMethods(postRecruitment.getProgressMethods())
                .recruitmentTechStacks(new ArrayList<>())
                .numberOfPeople(postRecruitment.getNumberOfPeople())
                .progressPeriod(postRecruitment.getProgressPeriod())
                .recruitmentDeadline(postRecruitment.getRecruitmentDeadline())
                .contract(postRecruitment.getContract())
                .subject(postRecruitment.getSubject())
                .content(postRecruitment.getContent())
                .build();
    }

    public void update(UpdateRecruitment updateRecruitment) {
        this.recruitmentCategories = updateRecruitment.getRecruitmentCategories();
        this.progressMethods = updateRecruitment.getProgressMethods();
        this.numberOfPeople = updateRecruitment.getNumberOfPeople();
        this.progressPeriod = updateRecruitment.getProgressPeriod();
        this.recruitmentDeadline = updateRecruitment.getRecruitmentDeadline();
        this.contract = updateRecruitment.getContract();
        this.subject = updateRecruitment.getSubject();
        this.content = updateRecruitment.getContent();
    }

    public void clearTechStacksAndPositions() {
        this.recruitmentTechStacks.clear();
        this.recruitmentPositions.clear();
    }

    public void makeRelationshipWithRecruitmentTechStack(RecruitmentTechStack recruitmentTechStack) {
        this.recruitmentTechStacks.add(recruitmentTechStack);
    }

    public void makeRelationshipWithRecruitmentPosition(RecruitmentPosition recruitmentPosition) {
        this.recruitmentPositions.add(recruitmentPosition);
    }

    public void makeRelationshipWithRecruitmentUser(User user) {
        var recruitmentUser = RecruitmentUser.of(this, user);
        this.recruitmentUser = recruitmentUser;
        user.getRecruitmentUsers().add(recruitmentUser);
    }

    public void plusViews() {
        this.views++;
    }
}
