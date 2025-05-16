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
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String positionName;

    @OneToMany(mappedBy = "position")
    private List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();

    private Position(String positionName) {
        this.positionName = positionName;
    }

    public static Position from(String position) {
        return new Position(position);
    }

    public void makeRelationship(RecruitmentPosition recruitmentPosition) {
        recruitmentPositions.add(recruitmentPosition);
    }
}
