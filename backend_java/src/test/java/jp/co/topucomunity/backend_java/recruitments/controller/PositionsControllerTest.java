package jp.co.topucomunity.backend_java.recruitments.controller;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
class PositionsControllerTest {

    private final PositionsRepository positionsRepository;
    private final MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        positionsRepository.deleteAll();
    }

    @DisplayName("응모 포지션 목록을 반환할 수 있다.")
    @Test
    void getPositions() throws Exception {
        // given
        var backend = Position.from("Backend");
        var infra = Position.from("Infra");

        positionsRepository.saveAll(List.of(backend, infra));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/positions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.positions.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.positions.[0]", Matchers.is("Backend")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.positions.[1]", Matchers.is("Infra")))
                .andDo(print());

    }

}