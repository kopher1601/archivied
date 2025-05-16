package jp.co.topucomunity.backend_java.recruitments.domain;

import jakarta.persistence.*;
import jp.co.topucomunity.backend_java.users.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private User user;

    private RecruitmentUser(Recruitment recruitment, User user) {
        this.recruitment = recruitment;
        this.user = user;
    }

    public static RecruitmentUser of(Recruitment recruitment, User user) {
        return new RecruitmentUser(recruitment, user);
    }
}
