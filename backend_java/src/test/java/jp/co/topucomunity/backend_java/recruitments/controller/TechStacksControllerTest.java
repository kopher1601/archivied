package jp.co.topucomunity.backend_java.recruitments.controller;

import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.recruitments.repository.TechStacksRepository;
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
@RequiredArgsConstructor
@SpringBootTest
class TechStacksControllerTest {

    private final TechStacksRepository techStacksRepository;
    private final MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        techStacksRepository.deleteAll();
    }

    @DisplayName("응모 기술 목록을 반환할 수 있다.")
    @Test
    void getTechStacks() throws Exception {
        // given
        var spring = TechStack.from("Spring");
        var kotlin = TechStack.from("Kotlin");
        var ktor = TechStack.from("Ktor");

        techStacksRepository.saveAll(List.of(spring, kotlin, ktor));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/tech-stacks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.techStacks.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techStacks.[0]", Matchers.is("Spring")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techStacks.[1]", Matchers.is("Kotlin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.techStacks.[2]", Matchers.is("Ktor")))
                .andDo(print());

    }
}