package jp.co.topucomunity.backend_java.recruitments.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentTechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TechStack techStack;


    private RecruitmentTechStack(TechStack techStack, Recruitment recruitment) {
        this.techStack = techStack;
        this.recruitment = recruitment;
    }

    public static RecruitmentTechStack of(TechStack techStack, Recruitment recruitment) {
        return new RecruitmentTechStack(techStack, recruitment);
    }

    public void makeRelationship(TechStack techStack, Recruitment recruitment) {
        techStack.makeRelationship(this);
        recruitment.makeRelationshipWithRecruitmentTechStack(this);
    }

    // 디미터 법칙 준수를 위해
    public String getTechnologyName() {
        return this.techStack.getTechnologyName();
    }

}
