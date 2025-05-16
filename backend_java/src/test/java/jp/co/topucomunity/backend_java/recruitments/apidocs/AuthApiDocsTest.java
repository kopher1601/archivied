package jp.co.topucomunity.backend_java.recruitments.apidocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.topucomunity.backend_java.config.OAuth2UserPrincipal;
import jp.co.topucomunity.backend_java.recruitments.config.TopuMockUser;
import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import jp.co.topucomunity.backend_java.users.controller.in.SignUpRequest;
import jp.co.topucomunity.backend_java.users.domain.User;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class AuthApiDocsTest {

    private final UserRepository userRepository;
    private final PositionsRepository positionsRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        positionsRepository.deleteAllInBatch();
    }

    @DisplayName("유저 ID 로 유저 정보를 조회할 수 있다")
    @Test
    void getUserById() throws Exception {
        // given
        var backend = Position.from("Backend");


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
        var savedUser = userRepository.save(user);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auth/{userId}", savedUser.getUserId()))
                .andDo(MockMvcRestDocumentation.document("get-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("userId").description("유저 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("userId").description("유저 ID"),
                                PayloadDocumentation.fieldWithPath("email").description("유저 Email 주소"),
                                PayloadDocumentation.fieldWithPath("nickname").description("유저 닉네임"),
                                PayloadDocumentation.fieldWithPath("position").description("유저 직책"),
                                PayloadDocumentation.fieldWithPath("techStacks").description("유저 관심 기술 스택"),
                                PayloadDocumentation.fieldWithPath("affiliation").description("유저 소속"),
                                PayloadDocumentation.fieldWithPath("isPublicAffiliation").description("유저 소속 공개 플래그"),
                                PayloadDocumentation.fieldWithPath("personalHistoryYear").description("유저 경력"),
                                PayloadDocumentation.fieldWithPath("selfIntroduction").description("유저 자기소개"),
                                PayloadDocumentation.fieldWithPath("links").description("유저 링크")
                        )
                ))
                .andDo(print());
    }

    @DisplayName("지정한 유저 ID 로 유저를 찾을 수 없으면 에러메시지를 반환한다.")
    @Test
    void getUserById_Fail() throws Exception {
        // given
        var userId = 99L;

        // Todo : .andExpect() 추가
        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auth/{userId}", userId))
                .andDo(MockMvcRestDocumentation.document("get-user-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("userId").description("유저 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("errorMessage").description("에러 메시지"),
                                PayloadDocumentation.fieldWithPath("validationErrors").description("검증 에러")
                        )
                ))
                .andDo(print());
    }

    @TopuMockUser
    @DisplayName("첫 로그인 유저의 경우 회원등록 절차를 진행한다.")
    @Test
    void signUp() throws Exception {

        // given
        var signUpRequest = SignUpRequest.createSignUpRequest(
                "Bomb",
                "Infra",
                30,
                List.of("Cobol", "C", "PHP", "Basic"));
        var jsonString = objectMapper.writeValueAsString(signUpRequest);

        var principal = (OAuth2UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/signup")
                        .cookie(new MockCookie("SESSION", principal.getJws()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("create-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("nickname").description("닉네임"),
                                PayloadDocumentation.fieldWithPath("positionName").description("직무"),
                                PayloadDocumentation.fieldWithPath("personalHistoryYear").description("경력년수").attributes(key("constraint").value("숫자만 입력")),
                                PayloadDocumentation.fieldWithPath("techStackNames").description("관심 기술스택")
                        )
                ))
                .andDo(print());

    }
}
