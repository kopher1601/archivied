package jp.co.topucomunity.backend_java.users.domain;

import jakarta.persistence.*;
import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentUser;
import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.users.application.port.out.SignupUser;
import jp.co.topucomunity.backend_java.users.usecase.in.RegisterUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "users")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String sub;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String affiliation;

    private boolean isPublicAffiliation;

    @ColumnDefault(value = "true")
    private boolean isFirstLogin;

    private Integer personalHistoryYear;

    @Lob
    private String selfIntroduction;

    private String links;

    private String picture;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RecruitmentUser> recruitmentUsers = new ArrayList<>();

    @Builder
    public User(String sub, String email, String nickname, Position position, String affiliation, boolean isPublicAffiliation, boolean isFirstLogin, Integer personalHistoryYear, String selfIntroduction, List<TechStack> techStacks, String links, String picture) {
        this.sub = sub;
        this.email = email;
        this.nickname = nickname;
        this.position = position;
        this.affiliation = affiliation;
        this.isPublicAffiliation = isPublicAffiliation;
        this.isFirstLogin = isFirstLogin;
        this.personalHistoryYear = personalHistoryYear;
        this.selfIntroduction = selfIntroduction;
        this.techStacks = techStacks;
        this.links = links;
        this.picture = picture;
    }

    public static User from(OidcUser oidcUser) {
        return User.builder()
                .sub(oidcUser.getSubject())
                .email(oidcUser.getEmail())
                .picture(oidcUser.getPicture())
                .isFirstLogin(true)
                .build();
    }

    public static User of(String sub, String email, String picture) {
        return User.builder()
                .sub(sub)
                .email(email)
                .picture(picture)
                .isFirstLogin(true)
                .build();
    }

    public static User createFirstLoggedInUser(String email, String nickname, Position position, String affiliation, boolean isPublicAffiliation, Integer personalHistoryYear, String selfIntroduction, String links, boolean isFirstLogin) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .position(position)
                .affiliation(affiliation)
                .isPublicAffiliation(isPublicAffiliation)
                .isFirstLogin(isFirstLogin)
                .personalHistoryYear(personalHistoryYear)
                .selfIntroduction(selfIntroduction)
                .links(links)
                .build();
    }

    public void registerFirstLoginUserInfo(RegisterUser registerUser) {
        this.nickname = registerUser.getNickname();
        this.position = Position.from(registerUser.getPositionName());
        this.personalHistoryYear = registerUser.getPersonalHistoryYear();
        this.isPublicAffiliation = false;
        this.isFirstLogin = false;
        this.techStacks = registerUser.getTechStackNames().stream()
                .map(TechStack::from)
                .toList();
    }

    public void updateFirstLoginUser(SignupUser signupUser) {
        this.nickname = signupUser.getNickname();
        this.position = signupUser.getPosition();
        this.personalHistoryYear = signupUser.getPersonalHistoryYear();
        this.isPublicAffiliation = false;
        this.isFirstLogin = false;
        this.techStacks = signupUser.getTechStacks();
    }
}
