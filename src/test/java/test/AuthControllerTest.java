package test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import config.MemberConfig;
import config.MvcConfig;
import controller.AuthController;
import dto.EmailSendRequest;
import service.EmailVerificationService;

@WebAppConfiguration
@ContextConfiguration(classes= {MemberConfig.class, MvcConfig.class})
@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
class AuthControllerTest {

    @Mock
    private EmailVerificationService verificationService; // 가짜(모의) 객체

    // 실제로 테스트할 컨트롤러
    @InjectMocks
    private AuthController authController;

    // MockMvc: 실제 서버 구동 없이 컨트롤러를 테스트하기 위한 도구
    private MockMvc mockMvc;

    // JSON 직렬화를 위해
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // AuthController에 Mock 주입이 이뤄진 상태로 MockMvc 구성
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("이메일 전송 성공 테스트")
    void testSendVerificationCode_Success() throws Exception {
        // given: 컨트롤러에 전달할 RequestBody
        EmailSendRequest request = new EmailSendRequest();
        request.setEmail("test@dankook.ac.kr");

        // 가짜 서비스(verificationService)가 반환할 값(성공 시나리오)
        Map<String, Object> successResult = new HashMap<>();
        successResult.put("success", true);
        successResult.put("message", "인증 코드가 발송되었습니다.");

        // when: 서비스의 특정 메서드 호출 시, mock 동작 정의
        when(verificationService.requestVerificationCode(
                eq("test@dankook.ac.kr"), // 실제 값도 가능
                eq("단국대학교"),
                eq(true)))
            .thenReturn(successResult);

        // then: MockMvc를 사용해 POST 요청 시도 및 응답 검증
        mockMvc.perform(post("/auth/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());  // HTTP 200
//            .andExpect(jsonPath("$.success").value(true))
//            .andExpect(jsonPath("$.message").value("인증 코드가 발송되었습니다."));
    }

    @Test
    @DisplayName("이메일 전송 실패 테스트 - 대학 도메인 불일치")
    void testSendVerificationCode_Fail() throws Exception {
        // given
        EmailSendRequest request = new EmailSendRequest();
        request.setEmail("fail@unknown.ac.kr");

        // 가짜 서비스가 반환할 값(실패 시나리오)
        Map<String, Object> failResult = new HashMap<>();
        failResult.put("success", false);
        failResult.put("message", "대학 도메인과 불일치");

        when(verificationService.requestVerificationCode(
                eq("fail@unknown.ac.kr"),
                eq("단국대학교"),
                eq(true)))
            .thenReturn(failResult);

        // then
        mockMvc.perform(post("/auth/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());  // HTTP 400
//            .andExpect(jsonPath("$.success").value(false))
//            .andExpect(jsonPath("$.message").value("대학 도메인과 불일치"));
    }
}
