package test;

import dto.UserDTO;
import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MemberConfig;
import config.MvcConfig;
import controller.UserController;
import security.JwtUtil;
import service.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
@ContextConfiguration(classes= {MemberConfig.class, MvcConfig.class})
@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
@Log4j2
class UserControllerTest {
	 private MockMvc mockMvc;

	    @Autowired
	    private UserController userController;


	    private static final String VALID_TOKEN =
	            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1wbGVAZGFua29vay5hYy5rciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3MzU4MzI3ODgsImV4cCI6MTczNzAxNTU4OH0.fXS-LO6JDvQl4UvaxXzDb01oU8SV3Z8u440jK1KR0rs";


	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	        // AuthController에 Mock 주입이 이뤄진 상태로 MockMvc 구성
	        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	    }




	    @Test
	    public void getUserInfo_ValidToken_ShouldReturnOk() throws Exception {
	        // JWT Util Mocking
	        try (MockedStatic<JwtUtil> mockedJwt = Mockito.mockStatic(JwtUtil.class)) {
	            mockedJwt.when(() -> JwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
	            mockedJwt.when(() -> JwtUtil.extractUsername(VALID_TOKEN))
	                    .thenReturn("sample@dankook.ac.kr");

	            mockMvc.perform(get("/user/info")
	                            .header("Authorization", "Bearer " + VALID_TOKEN))
	                    .andExpect(status().isOk())
	                    .andExpect(jsonPath("$.success").value(true))
	                    .andExpect(jsonPath("$.statusCode").value(200))
	                    .andExpect(jsonPath("$.nickname").value("sample"))
	                    .andExpect(jsonPath("$.email").value("sample@dankook.ac.kr"));
	        }
	    }

	    @Test
	    public void getUserInfo_MissingToken_ShouldReturn401() throws Exception {
	        mockMvc.perform(get("/user/info"))
	                .andExpect(status().isUnauthorized())
	                .andExpect(jsonPath("$.success").value(false))
	                .andExpect(jsonPath("$.statusCode").value(401))
	                .andExpect(jsonPath("$.message").value("JWT 토큰이 제공되지 않았습니다."));
	    }
	}