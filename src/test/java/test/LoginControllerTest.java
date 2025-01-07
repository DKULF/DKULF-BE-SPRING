package test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.MemberConfig;
import config.MvcConfig;
import controller.LoginController;
import dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.UserService;
@WebAppConfiguration
@ContextConfiguration(classes= {MemberConfig.class, MvcConfig.class})
@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
class LoginControllerTest {


    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    // MockMvc: 실제 서버 구동 없이 컨트롤러를 테스트하기 위한 도구
    private MockMvc mockMvc;

    // JSON 직렬화를 위해
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // AuthController에 Mock 주입이 이뤄진 상태로 MockMvc 구성
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("sample@dankook.ac.kr");
        loginDTO.setPassword("sample");

        when(userService.validateUser("sample@dankook.ac.kr", "sample")).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token.accessToken").exists())
                .andExpect(jsonPath("$.token.refreshToken").exists());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongpassword");

        when(userService.validateUser("test@example.com", "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1wbGVAZGFua29vay5hYy5rciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3MzU4MzI3ODgsImV4cCI6MTczNzMwMTU4OH0.fXS-LO6JDvQl4UvaxXzDb01oU8SV3Z8u440jK1KR0rs";

        // 성공 시나리오: 토큰 검증 로직 제외
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void testRefreshToken_InvalidToken() throws Exception {
        String refreshToken = "invalid-refresh-token";

        // 실패 시나리오: 간단한 실패 응답 처리
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Refresh token이 만료되었습니다."));
    }


}
