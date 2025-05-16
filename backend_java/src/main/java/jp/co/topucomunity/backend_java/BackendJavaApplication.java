package jp.co.topucomunity.backend_java;

import jp.co.topucomunity.backend_java.recruitments.controller.in.CreateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import jp.co.topucomunity.backend_java.recruitments.usecase.RecruitmentsUsecase;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.PostRecruitment;
import jp.co.topucomunity.backend_java.users.domain.User;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class BackendJavaApplication {

    private final UserRepository userRepository;
    private final RecruitmentsUsecase recruitmentsUsecase;

    public static void main(String[] args) {
        SpringApplication.run(BackendJavaApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            var user = User.createFirstLoggedInUser("test@test.com", "kakao", Position.from("backend"), "Topu-team", false, 3, "test용 유저입니다.", "", false);
            var savedUser = userRepository.save(user);

            var postRecruitments = IntStream.range(0, 50).mapToObj((i) -> PostRecruitment.of(CreateRecruitmentRequest.builder()
                    .recruitmentCategories(RecruitmentCategories.STUDY)
                    .progressMethods(ProgressMethods.ALL)
                    .techStacks(List.of("Java" + i, "Spring", "JPA"))
                    .recruitmentPositions(List.of("Backend", "DevOps", "Infra"))
                    .numberOfPeople(3 + i)
                    .progressPeriod(6 + i)
                    .recruitmentDeadline(LocalDate.of(2024, 10, 30))
                    .contract(String.format("test$s@test.com", i))
                    .subject("새로운 서비스를 개발하실 분을 모집합니다.")
                    .content("현실은 SI, SES, 개레거시지롱")
                    .build(), savedUser.getUserId())).toList();
            postRecruitments.forEach(recruitmentsUsecase::post);
        };
    }
}
