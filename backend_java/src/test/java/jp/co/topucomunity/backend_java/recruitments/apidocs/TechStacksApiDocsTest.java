package jp.co.topucomunity.backend_java.recruitments.apidocs;

import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.recruitments.repository.TechStacksRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
public class TechStacksApiDocsTest {

    private final MockMvc mvc;
    private final TechStacksRepository techStacksRepository;

    @BeforeEach
    void setUp() {
        techStacksRepository.deleteAll();
    }

    @DisplayName("기술스택의 전체목록을 확인할 수 있다.")
    @Test
    void getTechStacks() throws Exception {
        // given
        var java = TechStack.from("Java");
        var go = TechStack.from("Go");
        var aws = TechStack.from("AWS");
        var docker = TechStack.from("Docker");
        techStacksRepository.saveAll(List.of(java, go, aws, docker));

        // expected
        mvc.perform(RestDocumentationRequestBuilders.get("/tech-stacks"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("get-tech-stacks",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("techStacks").description("기술스택 전체목록")
                        )
                ))
                .andDo(print());
    }
}
