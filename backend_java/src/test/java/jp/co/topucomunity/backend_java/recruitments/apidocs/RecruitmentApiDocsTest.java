package jp.co.topucomunity.backend_java.recruitments.apidocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.topucomunity.backend_java.config.OAuth2UserPrincipal;
import jp.co.topucomunity.backend_java.recruitments.config.TopuMockUser;
import jp.co.topucomunity.backend_java.recruitments.controller.in.CreateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.controller.in.UpdateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.domain.*;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import jp.co.topucomunity.backend_java.recruitments.repository.*;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.PostRecruitment;
import jp.co.topucomunity.backend_java.users.domain.User;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jp.co.topucomunity.backend_java.users.domain.User.createFirstLoggedInUser;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
public class RecruitmentApiDocsTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final RecruitmentsRepository recruitmentsRepository;
    private final RecruitmentTechStacksRepository recruitmentTechStacksRepository;
    private final RecruitmentPositionsRepository recruitmentPositionsRepository;
    private final PositionsRepository positionsRepository;
    private final TechStacksRepository techStacksRepository;
    private final UserRepository userRepository;
    private final RecruitmentUserRepository recruitmentUserRepository;

    @BeforeEach
    void setUp() {
        recruitmentsRepository.deleteAll();
        recruitmentTechStacksRepository.deleteAll();
        recruitmentPositionsRepository.deleteAll();
        positionsRepository.deleteAll();
        techStacksRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        recruitmentUserRepository.deleteAll();
    }

    @DisplayName("응모 ID 를 이용하여 해당 응모글을 응모글 목록에서 제거할 수 있다.")
    @Test
    void deleteRecruitmentById() throws Exception {
        // given
        var techStack = TechStack.from("Java");
        var position = Position.from("Backend");
        var recruitment = Recruitment.builder()
                .recruitmentCategories(RecruitmentCategories.STUDY)
                .progressMethods(ProgressMethods.ALL)
                .numberOfPeople(3)
                .progressPeriod(3)
                .recruitmentDeadline(LocalDate.of(2024, 10, 30))
                .contract("test@tesc.om")
                .subject("끝내주는 서비스를 개발 해 봅시다.")
                .content("사실은 윈도우앱")
                .recruitmentTechStacks(new ArrayList<>())
                .build();
        var recruitmentPosition = RecruitmentPosition.of(position, recruitment);
        var recruitmentTechStack = RecruitmentTechStack.of(techStack, recruitment);
        recruitmentPosition.makeRelationship(position, recruitment);
        recruitmentTechStack.makeRelationship(techStack, recruitment);

        var savedRecruitment = recruitmentsRepository.save(recruitment);

        // expect
        mvc.perform(RestDocumentationRequestBuilders.delete("/recruitments/{recruitmentId}", savedRecruitment.getId()))
                .andExpect(status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("delete-recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("recruitmentId").description("응모 ID")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("응모글 ID로 응모글의 상세내용을 확인할 수 있다.")
    @Test
    void getRecruitmentById() throws Exception {

        // given
        var user = User.builder()
                .email("test-user@test.com")
                .nickname("test-user")
                .position(Position.from("Backend"))
                .isPublicAffiliation(false)
                .personalHistoryYear(5)
                .selfIntroduction("안녕하세요 우아한형제들에서 결제 서비스를 담당하고 있는 백엔드 엔지니어입니다.")
                .links("https://github.com/wooah/backend/god/king/good, https://github.com/wooah/backend/god/king/good")
                .isFirstLogin(true)
                .build();

        var techStack = TechStack.from("Java");
        var position = Position.from("Infra");
        var recruitment = Recruitment.builder()
                .recruitmentCategories(RecruitmentCategories.STUDY)
                .progressMethods(ProgressMethods.ALL)
                .numberOfPeople(3)
                .progressPeriod(3)
                .recruitmentDeadline(LocalDate.of(2024, 10, 30))
                .contract("test@tesc.om")
                .subject("끝내주는 서비스를 개발 해 봅시다.")
                .content("사실은 윈도우앱")
                .recruitmentTechStacks(new ArrayList<>())
                .build();
        var recruitmentPosition = RecruitmentPosition.of(position, recruitment);
        var recruitmentTechStack = RecruitmentTechStack.of(techStack, recruitment);
        recruitmentPosition.makeRelationship(position, recruitment);
        recruitmentTechStack.makeRelationship(techStack, recruitment);

        recruitment.makeRelationshipWithRecruitmentUser(user);

        var savedRecruitment = recruitmentsRepository.save(recruitment);

        // expected
        mvc.perform(RestDocumentationRequestBuilders.get("/recruitments/{recruitmentId}", savedRecruitment.getId()))
                .andExpect(status().isOk())
                .andDo(document("get-recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("recruitmentId").description("응모글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("id").description("응모글 ID"),
                                fieldWithPath("userId").description("작성자 ID"),
                                fieldWithPath("nickname").description("작성자 이름"),
                                fieldWithPath("recruitmentCategories").description("응모 카테고리"),
                                fieldWithPath("progressMethods").description("진행 방법"),
                                fieldWithPath("numberOfPeople").description("모집 인원"),
                                fieldWithPath("progressPeriod").description("진행 기간"),
                                fieldWithPath("recruitmentDeadline").description("마감일"),
                                fieldWithPath("contract").description("연락처"),
                                fieldWithPath("subject").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("techStacks").description("기술스택"),
                                fieldWithPath("positions").description("응모 포지션")
                        )))
                .andDo(print());
    }

    @TopuMockUser
    @DisplayName("응모글을 작성하면 응모글 목록에 담긴다.")
    @Test
    void postRecruitment() throws Exception {

        // given
        var request = createDefaultRecruitmentRequest();
        var jsonString = objectMapper.writeValueAsString(request);

        var principal = (OAuth2UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // expected
        mvc.perform(RestDocumentationRequestBuilders.post("/recruitments")
                        .cookie(new MockCookie("SESSION", principal.getJws()))
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("recruitmentCategories").description("응모 카테고리"),
                                fieldWithPath("progressMethods").description("응모 방법"),
                                fieldWithPath("techStacks").description("기술 스택"),
                                fieldWithPath("recruitmentPositions").description("응모 포지션"),
                                fieldWithPath("numberOfPeople").description("모집 인원"),
                                fieldWithPath("progressPeriod").description("진행 기간"),
                                fieldWithPath("recruitmentDeadline").description("응모 마감일"),
                                fieldWithPath("contract").description("연락처"),
                                fieldWithPath("subject").description("제목"),
                                fieldWithPath("content").description("내용")
                        )));
    }

    @DisplayName("작성한 응모글을 수정한다.")
    @Test
    void updateRecruitment() throws Exception {
        var savedUser = userRepository.save(createFirstLoggedInUser(
                "test-user@test.com",
                "test-user",
                Position.from("Frontend"),
                "우아한형제들",
                false,
                5,
                "안녕하세요 우아한형제들에서 결제 서비스를 담당하고 있는 백엔드 엔지니어입니다.",
                "https://github.com/wooah/backend/god/king/good, https://github.com/wooah/backend/god/king/good",
                true
        ));

        // given
        var createRecruitmentRequest = createDefaultRecruitmentRequest();
        var postRecruitment = PostRecruitment.of(createRecruitmentRequest, savedUser.getUserId());
        var recruitment = Recruitment.from(postRecruitment);
        recruitmentsRepository.save(recruitment);

        var request = createUpdateRecruitmentRequest();

        var jsonString = objectMapper.writeValueAsString(request);

        // expected
        mvc.perform(RestDocumentationRequestBuilders.put("/recruitments/{recruitmentId}", recruitment.getId())
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("update-recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("recruitmentId").description("응모글 ID")
                        ),
                        requestFields(
                                fieldWithPath("recruitmentCategories").description("응모 카테고리"),
                                fieldWithPath("progressMethods").description("진행 방법"),
                                fieldWithPath("techStacks").description("기술스택"),
                                fieldWithPath("recruitmentPositions").description("응모 포지션"),
                                fieldWithPath("numberOfPeople").description("모집 인원"),
                                fieldWithPath("progressPeriod").description("진행 기간"),
                                fieldWithPath("recruitmentDeadline").description("마감일"),
                                fieldWithPath("contract").description("연락처"),
                                fieldWithPath("subject").description("제목"),
                                fieldWithPath("content").description("내용")
                        )));
    }

    @DisplayName("메인페이지에서 기술스택, 포지션, 진행방식, 카테고리, 제목을 지정해서 검색할 수 있고 그 목록을 확인할 수 있다.")
    @Test
    void searchResult() throws Exception {
        // given
        var user = User.builder()
                .email("test-user@test.com")
                .nickname("test-user")
                .position(Position.from("Backend"))
                .isPublicAffiliation(false)
                .personalHistoryYear(5)
                .selfIntroduction("안녕하세요 우아한형제들에서 결제 서비스를 담당하고 있는 백엔드 엔지니어입니다.")
                .links("https://github.com/wooah/backend/god/king/good, https://github.com/wooah/backend/god/king/good")
                .isFirstLogin(true)
                .build();

        var recruitment1 = createRecruitment(TechStack.from("Java"), Position.from("バックエンド"), RecruitmentCategories.STUDY, ProgressMethods.ALL);
        var recruitment2 = createRecruitment(TechStack.from("React"), Position.from("フロントエンド"), RecruitmentCategories.PROJECT, ProgressMethods.ONLINE);
        var recruitment3 = createRecruitment(TechStack.from("Vue"), Position.from("Frontend"), RecruitmentCategories.PROJECT, ProgressMethods.ONLINE);
        var recruitment4 = createRecruitment(TechStack.from("Docker"), Position.from("インフラ"), RecruitmentCategories.STUDY, ProgressMethods.ONLINE);
        var recruitment5 = createRecruitment(TechStack.from("AWS"), Position.from("DevOps"), RecruitmentCategories.PROJECT, ProgressMethods.OFFLINE);

        recruitment1.makeRelationshipWithRecruitmentUser(user);
        recruitment2.makeRelationshipWithRecruitmentUser(user);
        recruitment3.makeRelationshipWithRecruitmentUser(user);
        recruitment4.makeRelationshipWithRecruitmentUser(user);
        recruitment5.makeRelationshipWithRecruitmentUser(user);

        recruitmentsRepository.saveAll(List.of(recruitment1, recruitment2, recruitment3, recruitment4, recruitment5));

        var searchQueries = "page=1&size=10&categories=STUDY&positions=バックエンド,フロントエンド&progressMethods=ALL&techStacks=React&search=끝내주는";
        // expected
        mvc.perform(RestDocumentationRequestBuilders.get("/recruitments/query?" + searchQueries))
                .andExpect(status().isOk())
                .andDo(document("get-recruitments",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("page").description("페이지 번호"),
                                RequestDocumentation.parameterWithName("size").description("페이지당 표시 갯수"),
                                RequestDocumentation.parameterWithName("categories").description("응모 카테고리"),
                                RequestDocumentation.parameterWithName("positions").description("응모 포지션"),
                                RequestDocumentation.parameterWithName("techStacks").description("기술스택"),
                                RequestDocumentation.parameterWithName("progressMethods").description("진행방식"),
                                RequestDocumentation.parameterWithName("search").description("제목")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("count").description("응모글 전체 갯수"),
                                fieldWithPath("data[].id").description("응모글 ID"),
                                fieldWithPath("data[].nickname").description("작성자 닉네임"),
                                fieldWithPath("data[].userId").description("작성자 ID"),
                                fieldWithPath("data[].recruitmentCategory").description("응모 카테고리"),
                                fieldWithPath("data[].progressMethods").description("진행 방법"),
                                fieldWithPath("data[].recruitmentDeadline").description("마감일"),
                                fieldWithPath("data[].subject").description("제목"),
                                fieldWithPath("data[].techStacks").description("기술스택"),
                                fieldWithPath("data[].positions").description("응모 포지션"),
                                fieldWithPath("data[].views").description("조회수")
                        )))
                .andDo(print());

    }

    private static Recruitment createRecruitment(TechStack techStack, Position position, RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods) {
        var recruitment = Recruitment.builder()
                .recruitmentCategories(recruitmentCategories == null ? RecruitmentCategories.STUDY : recruitmentCategories)
                .progressMethods(progressMethods == null ? ProgressMethods.ALL : progressMethods)
                .numberOfPeople(3)
                .progressPeriod(3)
                .recruitmentDeadline(LocalDate.of(2024, 10, 30))
                .contract("test@tesc.om")
                .subject("끝내주는 서비스를 개발 해 봅시다.")
                .content("사실은 윈도우앱")
                .recruitmentTechStacks(new ArrayList<>())
                .build();
        var recruitmentPosition = RecruitmentPosition.of(position, recruitment);
        var recruitmentTechStack = RecruitmentTechStack.of(techStack, recruitment);
        recruitmentPosition.makeRelationship(position, recruitment);
        recruitmentTechStack.makeRelationship(techStack, recruitment);
        return recruitment;
    }

    private static CreateRecruitmentRequest createDefaultRecruitmentRequest() {
        return CreateRecruitmentRequest.builder()
                .recruitmentCategories(RecruitmentCategories.STUDY)
                .progressMethods(ProgressMethods.ALL)
                .techStacks(List.of("Java", "Spring", "JPA"))
                .recruitmentPositions(List.of("Backend", "DevOps", "Infra"))
                .numberOfPeople(3)
                .progressPeriod(6)
                .recruitmentDeadline(LocalDate.of(2024, 10, 30))
                .contract("test@tesc.om")
                .subject("새로운 서비스를 개발하실 분을 모집합니다.")
                .content("현실은 SI, SES, 개레거시지롱")
                .build();
    }

    private static UpdateRecruitmentRequest createUpdateRecruitmentRequest() {
        return UpdateRecruitmentRequest.builder()
                .recruitmentCategories(RecruitmentCategories.PROJECT)
                .progressMethods(ProgressMethods.ONLINE)
                .techStacks(List.of("Python", "Go"))
                .recruitmentPositions(List.of("Backend22", "DevOps22", "Infra22"))
                .numberOfPeople(93)
                .progressPeriod(90)
                .recruitmentDeadline(LocalDate.of(2024, 12, 25))
                .contract("updatedEmail@test.com")
                .subject("수정된 제목")
                .content("수정된 본문")
                .build();
    }
}
