package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.LoginDTO;
import dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import mapper.UserMapper;
import security.JwtUtil;
import service.UserService;
import swagger.RefreshTokenRequest;
import swagger.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Default")
@RequestMapping("/auth")
public class LoginController {

	  	@Autowired
	  	private UserService userService;

	    @PostMapping("/login")
	    @ApiOperation(value = "로그인 API", notes = "사용자의 아이디와 비밀번호로 로그인을 처리합니다.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "로그인 성공", response = Response.LoginSuccessResponse.class),
	            @ApiResponse(code = 400, message = "잘못된 아이디, 비밀번호", response = Response.LoginErrorResponse1.class),
	            @ApiResponse(code = 401, message = "아이디, 비밀번호 누락", response = Response.LoginErrorResponse2.class)
	    })
	    public ResponseEntity<?> login(@RequestBody LoginDTO userDTO) {
	    	Map<String, Object> response = new HashMap<String, Object>();

	        String username = userDTO.getUsername();
	        String password = userDTO.getPassword();
	        // 사용자 인증
	    	   if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
	               response.put("statusCode", 400);
	               response.put("success", false);
	               response.put("message", "아이디 또는 비밀번호가 입력되지 않았습니다.");
	               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	           }
	    	   
	        if (userService.validateUser(userDTO.getUsername(), userDTO.getPassword())) {
	            // 액세스 토큰과 리프레시 토큰 생성
	        	
	        	String role = userService.getUserRole(userDTO.getUsername());
	            String accessToken = JwtUtil.generateAccessToken(userDTO.getUsername(),role);
	            String refreshToken = JwtUtil.generateRefreshToken(userDTO.getUsername());
	            response.put("success", true);
	            response.put("token", new AuthResponse(accessToken, refreshToken));
	            return ResponseEntity.ok(response);
	        }
            response.put("statusCode", 400);
            response.put("success", false);
            response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	    }

	    @PostMapping("/refresh")
	    @ApiOperation(value = "토큰 갱신 API", notes = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Access Token 발급 성공", response = Response.TokenSuccessResponse.class),
	            @ApiResponse(code = 400, message = "Refresh Token 만료", response = Response.RefreshTokenExpiredResponse.class),
	            @ApiResponse(code = 401, message = "유효하지 않은 Refresh Token", response = Response.InvalidRefreshTokenResponse.class)
	    })
	    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
	    	String refreshToken = request.getRefreshToken();
	    	 Map<String, Object> response = new HashMap<>();
	        // 리프레시 토큰 검증
	        if (JwtUtil.validateToken(refreshToken)) {
	            String username = JwtUtil.extractUsername(refreshToken);
	            String role = userService.getUserRole(username);
	            String newAccessToken = JwtUtil.generateAccessToken(username,role);

	            response.put("statusCode", 200);
	            response.put("success", true);
	            response.put("accessToken", newAccessToken);
	            return ResponseEntity.ok(response);
	        }
	        // 유효하지 않은 토큰 처리
	        if (JwtUtil.isTokenExpired(refreshToken)) {
	            response.put("statusCode", 401);
	            response.put("success", false);
	            response.put("message", "Refresh token이 만료되었습니다.");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        
	        response.put("statusCode", 401);
	        response.put("success", false);
	        response.put("message", "유효하지 않은 Refresh token 입니다.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	
	    

	    static class AuthResponse {
	        private String accessToken;
	        private String refreshToken;

	        public AuthResponse(String accessToken, String refreshToken) {
	            this.accessToken = accessToken;
	            this.refreshToken = refreshToken;
	        }

	        public String getAccessToken() {
	            return accessToken;
	        }

	        public String getRefreshToken() {
	            return refreshToken;
	        }
	    }
	}