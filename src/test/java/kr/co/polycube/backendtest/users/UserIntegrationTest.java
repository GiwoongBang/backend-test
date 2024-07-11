package kr.co.polycube.backendtest.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import kr.co.polycube.backendtest.users.dto.UserRequestDto;
import kr.co.polycube.backendtest.users.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        entityManager.createNativeQuery(
                "ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test @Order(1)
    void 유저_등록_후_조회() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("bang");
        String userRequestJson = objectMapper.writeValueAsString(userRequestDto);

        String userResponseJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDto userResponse = objectMapper.readValue(userResponseJson, UserResponseDto.class);
        Long id = userResponse.getId();

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("bang"));
    }

    @Test @Order(2)
    void 유저_등록_후_수정() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("bang");
        String userRequestJson = objectMapper.writeValueAsString(userRequestDto);

        String userResponseJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDto userResponse = objectMapper.readValue(userResponseJson, UserResponseDto.class);
        Long id = userResponse.getId();
        String updatedName = "changedBang";

        mockMvc.perform(put("/users/" + id)
                        .param("name", updatedName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

}
