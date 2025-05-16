package jp.co.topucomunity.backend_java.recruitments.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String technologyName;

    @OneToMany(mappedBy = "techStack")
    private List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();

    private TechStack(String technologyName) {
        this.technologyName = technologyName;
    }

    public static TechStack from(String technologyName) {
        return new TechStack(technologyName);
    }

    public void makeRelationship(RecruitmentTechStack recruitmentTechStack) {
        this.recruitmentTechStacks.add(recruitmentTechStack);
    }
}
