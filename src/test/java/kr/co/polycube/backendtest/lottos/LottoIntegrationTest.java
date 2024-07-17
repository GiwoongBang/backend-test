package kr.co.polycube.backendtest.lottos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LottoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 로또_번호_발급() throws Exception {
        String result = mockMvc.perform(post("/lottos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number1").value(between(1, 45)))
                .andExpect(jsonPath("$.number2").value(between(1, 45)))
                .andExpect(jsonPath("$.number3").value(between(1, 45)))
                .andExpect(jsonPath("$.number4").value(between(1, 45)))
                .andExpect(jsonPath("$.number5").value(between(1, 45)))
                .andExpect(jsonPath("$.number6").value(between(1, 45)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result);

        Set<Integer> numbers = new HashSet<>();
        numbers.add(jsonNode.get("number1").asInt());
        numbers.add(jsonNode.get("number2").asInt());
        numbers.add(jsonNode.get("number3").asInt());
        numbers.add(jsonNode.get("number4").asInt());
        numbers.add(jsonNode.get("number5").asInt());
        numbers.add(jsonNode.get("number6").asInt());

        assertThat(numbers).hasSize(6);
    }

    private Matcher<Integer> between(int min, int max) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item >= min && item <= max;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a number between " + min + " and " + max);
            }
        };
    }

}
