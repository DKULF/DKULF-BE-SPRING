package test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.MemberConfig;
import config.MvcConfig;
import controller.AuthController;
import controller.UserController;
import dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import security.JwtUtil;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

@WebAppConfiguration
@ContextConfiguration(classes= {MemberConfig.class, MvcConfig.class})
@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;
    // MockMvc: 실제 서버 구동 없이 컨트롤러를 테스트하기 위한 도구
    private MockMvc mockMvc;

    // JSON 직렬화를 위해
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private UserController userController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // AuthController에 Mock 주입이 이뤄진 상태로 MockMvc 구성
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void testGetUserInfo_Success() throws Exception {
        // Mock JWT token
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBkYW5rb29rLmFjLmtyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE3MzU4MzI3ODYsImV4cCI6MTczNzMwMTU4Nn0._MFmyKH48LVDmJKuwWq1_hhniobQ6VczBuXwsyxD--4";

//        // Mock user details
        UserDTO mockUser = new UserDTO();
        mockUser.setEmail("admin@dankook.ac.kr");
        mockUser.setNickname("admin");

//        // Mock JwtUtil behavior
//        when(JwtUtil.validateToken(validToken)).thenReturn(true);
//        when(JwtUtil.extractUsername(validToken)).thenReturn("admin@dankook.ac.kr");
//
//        // Mock userService behavior
//        when(userService.getUserByEmail("admin@dankook.ac.kr")).thenReturn(mockUser);
        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validateToken(validToken)).thenReturn(true);
            mockedJwtUtil.when(() -> JwtUtil.extractUsername(validToken)).thenReturn("admin@dankook.ac.kr");

            when(userService.getUserByEmail("admin@dankook.ac.kr")).thenReturn(mockUser);
        mockMvc.perform(get("/user/info")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.nickname").value("admin"))
//                .andExpect(jsonPath("$.email").value("admin@dankook.ac.kr"));
        }
    }

//    @Test
//    void testGetUserInfo_TokenMissing() throws Exception {
//        mockMvc.perform(get("/user/info"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("JWT 토큰이 제공되지 않았습니다."));
//    }
//
//    @Test
//    void testGetUserInfo_InvalidToken() throws Exception {
//        String invalidToken = "invalid.jwt.token";
//
//        // Mock JwtUtil behavior
//        when(JwtUtil.validateToken(invalidToken)).thenReturn(false);
//
//        mockMvc.perform(get("/user/info")
//                .header("Authorization", "Bearer " + invalidToken))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("JWT 토큰이 유효하지 않습니다."));
//    }
//
//    @Test
//    void testGetUserInfo_UserNotFound() throws Exception {
//        String validToken = "valid.jwt.token";
//
//        // Mock JwtUtil behavior
//        when(JwtUtil.validateToken(validToken)).thenReturn(true);
//        when(JwtUtil.extractUsername(validToken)).thenReturn("unknown@example.com");
//
//        // Mock userService behavior
//        when(userService.getUserByEmail("unknown@example.com")).thenReturn(null);
//
//        mockMvc.perform(get("/user/info")
//                .header("Authorization", "Bearer " + validToken))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("사용자 정보가 존재하지 않습니다."));
//    }
}
