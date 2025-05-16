package jp.co.topucomunity.backend_java.recruitments.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

//@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentPosition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Position position;

    @Builder
    public RecruitmentPosition(Recruitment recruitment, Position position) {
        this.recruitment = recruitment;
        this.position = position;
    }

    public static RecruitmentPosition of(Position position, Recruitment recruitment) {
        return new RecruitmentPosition(recruitment, position);
    }

    public void makeRelationship(Position position, Recruitment recruitment) {
        position.makeRelationship(this);
        recruitment.makeRelationshipWithRecruitmentPosition(this);
    }

    // 디미터 법칙 준수
    public String getPositionName() {
        return this.position.getPositionName();
    }
}
