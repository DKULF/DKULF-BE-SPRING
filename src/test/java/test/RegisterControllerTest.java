package test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.MemberConfig;
import config.MvcConfig;
import controller.RegisterController;
import dto.UserDTO;
import lombok.extern.log4j.Log4j2;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashMap;

@WebAppConfiguration
@ContextConfiguration(classes= {MemberConfig.class, MvcConfig.class})
@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
@Log4j2
class RegisterControllerTest {


    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;

    // MockMvc: 실제 서버 구동 없이 컨트롤러를 테스트하기 위한 도구
    private MockMvc mockMvc;

    // JSON 직렬화를 위해
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // AuthController에 Mock 주입이 이뤄진 상태로 MockMvc 구성
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }
    @Test
    void testRegisterUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setPasswordConfirm("password123");

        when(userService.isIdDuplicated(userDTO.getEmail())).thenReturn(false);

        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입이 성공적으로 완료되었습니다."));
    }

    @Test
    void testRegisterUser_EmailDuplicate() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setPasswordConfirm("password123");

        when(userService.isIdDuplicated(userDTO.getEmail())).thenReturn(true);

        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("이미 존재하는 Email입니다."));
    }

    @Test
    void testRegisterUser_PasswordTooShort() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("short");
        userDTO.setPasswordConfirm("short");

        when(userService.isIdDuplicated(userDTO.getEmail())).thenReturn(false);

        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("비밀번호는 8자 이상이어야 합니다."));
    }

    @Test
    void testRegisterUser_PasswordMismatch() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setPasswordConfirm("differentPassword");

        when(userService.isIdDuplicated(userDTO.getEmail())).thenReturn(false);

        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    void testRegisterUser_InternalServerError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setPasswordConfirm("password123");

        when(userService.isIdDuplicated(userDTO.getEmail())).thenReturn(false);
        doThrow(new RuntimeException("Internal error")).when(userService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/auth/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("회원가입에 실패했습니다."));
    }
}
