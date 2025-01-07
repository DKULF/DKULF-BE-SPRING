package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.LoginDTO;
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

@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "사용자 로그인 API", notes = "사용자의 아이디와 비밀번호로 로그인을 처리합니다.")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "로그인 성공", response = Response.LoginSuccessResponse.class),
        @ApiResponse(code = 400, message = "잘못된 아이디, 비밀번호", response = Response.LoginErrorResponse1.class),
        @ApiResponse(code = 401, message = "아이디, 비밀번호 누락", response = Response.LoginErrorResponse2.class)
})
public ResponseEntity<?> userLogin(@RequestBody LoginDTO loginDTO) {
    return handleLogin(loginDTO, false);
}

@PostMapping(value = "/admin/login", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "관리자 로그인 API", notes = "관리자의 아이디와 비밀번호로 로그인을 처리합니다.")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "로그인 성공", response = Response.LoginSuccessResponse.class),
        @ApiResponse(code = 400, message = "잘못된 아이디, 비밀번호", response = Response.LoginErrorResponse1.class),
        @ApiResponse(code = 401, message = "아이디, 비밀번호 누락", response = Response.LoginErrorResponse2.class),
        @ApiResponse(code = 403, message = "권한 없음", response = Response.LoginErrorResponse3.class)
})
public ResponseEntity<?> adminLogin(@RequestBody LoginDTO loginDTO) {
    return handleLogin(loginDTO, true);
}

private ResponseEntity<?> handleLogin(LoginDTO loginDTO, boolean isAdmin) {
    Map<String, Object> response = new HashMap<>();

    String username = loginDTO.getEmail();
    String password = loginDTO.getPassword();

    // 입력 값 검증
    if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
        response.put("statusCode", 401);
        response.put("success", false);
        response.put("message", "아이디 또는 비밀번호가 입력되지 않았습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 사용자 인증
    if (!userService.validateUser(username, password)) {
        response.put("statusCode", 400);
        response.put("success", false);
        response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 관리자 로그인 시 권한 확인
    String role = userService.getUserRole(username);
    if (isAdmin && !"ROLE_ADMIN".equals(role)) {
        response.put("statusCode", 403);
        response.put("success", false);
        response.put("message", "관리자 권한이 없습니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 토큰 생성
    String accessToken = JwtUtil.generateAccessToken(username, role);
    String refreshToken = JwtUtil.generateRefreshToken(username);

    response.put("success", true);
    response.put("token", new AuthResponse(accessToken, refreshToken));
    return ResponseEntity.ok(response);
}	
	    @DeleteMapping("/delete")
	    @ApiOperation(value = "회원 탈퇴 API", notes = "이메일을 사용하여 사용자를 삭제합니다.")
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "회원 탈퇴 성공", response = Response.DeleteSuccessResponse.class),
	            @ApiResponse(code = 404, message = "회원 정보 없음", response = Response.DeleteErrorResponse.class)
	    })
	    public ResponseEntity<?> deleteUser(@RequestParam @ApiParam(value = "사용자 이메일", required = true) String email) {
	        Map<String, Object> response = new HashMap<>();

	        // 유저 존재 여부 확인
	        if (!userService.userExists(email)) {
	            response.put("statusCode", 404);
	            response.put("success", false);
	            response.put("message", "회원 정보를 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        // 유저 삭제
	        userService.deleteUser(email);
	        response.put("statusCode", 200);
	        response.put("success", true);
	        response.put("message", "회원 탈퇴가 완료되었습니다.");
	        return ResponseEntity.ok(response);
	    }
	    
	 	@PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
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