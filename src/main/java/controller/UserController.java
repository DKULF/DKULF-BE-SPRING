package controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import security.JwtUtil;
import service.UserService;
import swagger.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Default")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "사용자 정보 조회 API", notes = "사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "사용자 정보 조회 성공", response = Response.UserInfoSuccessResponse.class),
            @ApiResponse(code = 401, message = "JWT 토큰 입력 안됨", response = Response.JwtErrorResponse.class),
            @ApiResponse(code = 402, message = "JWT 토큰이 유효하지 않음", response = Response.JwtErrorResponseValid.class),
            @ApiResponse(code = 404, message = "사용자 정보가 없음", response = Response.UserNotFoundResponse.class)
    })
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            response.put("success", false);
            response.put("statusCode", 401);
            response.put("message", "JWT 토큰이 제공되지 않았습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (authHeader.startsWith("Bearer ")) {
        	authHeader = authHeader.substring(7); // "Bearer " 접두사 제거
        }

        // Validate the JWT token
        if (!JwtUtil.validateToken(authHeader)) {
            response.put("success", false);
            response.put("statusCode", 401);
            response.put("message", "JWT 토큰이 유효하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Extract username from token
        String username = JwtUtil.extractUsername(authHeader);

        // Fetch user information
        UserDTO user = userService.getUserByEmail(username);
        if (user == null) {
            response.put("success", false);
            response.put("statusCode", 404);
            response.put("message", "사용자 정보가 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Success response
        response.put("success", true);
        response.put("statusCode", 200);
        response.put("nickname", user.getNickname());
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    }
}
