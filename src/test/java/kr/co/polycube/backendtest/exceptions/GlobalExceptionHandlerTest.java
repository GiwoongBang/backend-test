package kr.co.polycube.backendtest.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.polycube.backendtest.apis.UserController;
import kr.co.polycube.backendtest.users.UserService;
import kr.co.polycube.backendtest.users.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void 잘못된_입력_후_400_반환() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.reason").exists());
    }

    @Test
    void 사용자가_없을_때_400_반환() throws Exception {
        Mockito.when(userService.getUser(Mockito.anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.reason").value("유저를 찾을 수 없습니다."));
    }

    @Test
    void 매개변수_타입이_일치하지_않을_때_400_반환() throws Exception {
        mockMvc.perform(get("/users/invalid-id")) // id는 Long 타입인데 String 타입으로 전송
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.reason").exists());
    }

    @Test
    void JSON_형식이_맞지_않을_때_400_반환() throws Exception {
        String invalidJson = "{ \"name\": ";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.reason").exists());
    }

    @Test
    void 존재하지_않는_API_호출시_404_반환() throws Exception {
        mockMvc.perform(get("/invalid-url")) // 존재하지 않는 엔드 포인트
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.reason").exists());
    }

}