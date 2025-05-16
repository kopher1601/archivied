package jp.co.topucomunity.backend_java.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.topucomunity.backend_java.config.OAuth2UserPrincipal;
import jp.co.topucomunity.backend_java.recruitments.config.TopuMockUser;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import jp.co.topucomunity.backend_java.users.controller.in.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
class AuthControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final PositionsRepository positionsRepository;

    @BeforeEach
    void setUp() {
        positionsRepository.deleteAll();
    }

    @TopuMockUser
    @DisplayName("첫 로그인 유저의 경우 회원등록을 진행한다")
    @Test
    void signUp() throws Exception {

        // given
        var signUpRequest = SignUpRequest.createSignUpRequest(
                "Bomb",
                "Infra",
                30,
                List.of("Cobol", "C", "PHP", "Basic"));
        var jsonString = objectMapper.writeValueAsString(signUpRequest);

        var principal = (OAuth2UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // expected
        mvc.perform(RestDocumentationRequestBuilders.post("/auth/signup")
                        .cookie(new MockCookie("SESSION", principal.getJws()))
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

    }

}