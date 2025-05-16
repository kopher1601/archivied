package jp.co.topucomunity.backend_java.recruitments.apidocs;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import lombok.AllArgsConstructor;
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
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AllArgsConstructor
@SpringBootTest
public class PositionApiDocsTest {

    private final MockMvc mvc;
    private final PositionsRepository positionsRepository;

    @DisplayName("응모 포지션 목록을 확인할 수 있다.")
    @Test
    void getPositions() throws Exception {
        // given
        positionsRepository.saveAll(List.of(
                Position.from("Backend"),
                Position.from("バックエンド"),
                Position.from("Infra"),
                Position.from("インフラ"),
                Position.from("Frontend"),
                Position.from("フロントエンド")
        ));

        // expected
        mvc.perform(RestDocumentationRequestBuilders.get("/positions"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("get-positions",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("positions").description("응모 포지션 목록")
                        )
                ))
                .andDo(print());

    }
}
