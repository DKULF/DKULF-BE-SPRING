package controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.UserDTO;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    @ApiOperation(value = "사용자 정보 조회 API", notes = "사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "사용자 정보 조회 성공", response = Response.UserInfoSuccessResponse.class),
            @ApiResponse(code = 401, message = "JWT 토큰이 없거나 유효하지 않음", response = Response.JwtErrorResponse.class),
            @ApiResponse(code = 404, message = "사용자 정보가 없음", response = Response.UserNotFoundResponse.class)
    })
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("statusCode", 401);
            response.put("message", "JWT 토큰이 없거나 유효하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Validate the JWT token
        if (!JwtUtil.validateToken(token)) {
            response.put("success", false);
            response.put("statusCode", 401);
            response.put("message", "JWT 토큰이 없거나 유효하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Extract username from token
        String username = JwtUtil.extractUsername(token);

        // Fetch user information
        UserDTO user = userService.getUserByUsername(username);
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
